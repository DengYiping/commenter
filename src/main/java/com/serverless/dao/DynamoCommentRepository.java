package com.serverless.dao;

import static java.util.stream.StreamSupport.stream;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.serverless.types.Comment;

@Singleton
public class DynamoCommentRepository implements CommentRepository {
    private static final String POST_TITLE_COLUMN = "postTitle";
    private static final String LAST_UPDATED_COLUMN = "lastUpdated";
    private static final String COMMENT_COLUMN = "comment";

    private final Table table;

    private final LoadingCache<String, ImmutableSet<Comment>> blogCommentLoadingCache;

    private final Supplier<ImmutableSet<Comment>> allCommentCache;

    @Inject
    public DynamoCommentRepository(Table table) {
        this.table = table;
        blogCommentLoadingCache = CacheBuilder.newBuilder()
                .maximumSize(200)
                .expireAfterWrite(Duration.ofHours(1))
                .build(CacheLoader.from(this::getAllForPostFromDb));
        allCommentCache = Suppliers.memoizeWithExpiration(this::allFromDb, 1, TimeUnit.HOURS);
    }

    @Override
    public void insertOrUpdate(Comment comment) {
        table.updateItem(
                toPrimaryKey(comment),
                new AttributeUpdate(COMMENT_COLUMN).put(comment.getComment()));
    }

    @Override
    public boolean delete(Comment comment) {
        return table.deleteItem(toPrimaryKey(comment)).getItem() != null;
    }

    @Override
    public ImmutableSet<Comment> all() {
        return allCommentCache.get();
    }

    private ImmutableSet<Comment> allFromDb() {
        return stream(table.scan().pages().spliterator(), true)
                .flatMap(page -> stream(page.spliterator(), true))
                .map(DynamoCommentRepository::toComment)
                .collect(toImmutableSet());
    }

    @Override
    public ImmutableSet<Comment> getAllForPost(String postTitle) {
        return blogCommentLoadingCache.getUnchecked(postTitle);
    }

    private ImmutableSet<Comment> getAllForPostFromDb(String postTitle) {
        return stream(table.query(POST_TITLE_COLUMN, postTitle).pages().spliterator(), true)
                .flatMap(page -> stream(page.spliterator(), true))
                .map(DynamoCommentRepository::toComment)
                .collect(toImmutableSet());
    }

    private static PrimaryKey toPrimaryKey(Comment comment) {
        return new PrimaryKey(
                POST_TITLE_COLUMN,
                comment.getPostTitle(),
                LAST_UPDATED_COLUMN,
                comment.getLastUpdated().toString());
    }

    private static Comment toComment(Item item) {
        checkNotNull(item, "Empty result item");
        try {
            SimpleDateFormat format =
                    new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
            return Comment.builder()
                    .postTitle(item.getString(POST_TITLE_COLUMN))
                    .lastUpdated(format.parse(item.getString(LAST_UPDATED_COLUMN)))
                    .comment(item.getString(COMMENT_COLUMN))
                    .build();
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
}
