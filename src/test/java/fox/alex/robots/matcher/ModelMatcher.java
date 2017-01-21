package fox.alex.robots.matcher;

import org.junit.Assert;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ModelMatcher<T> {
    public interface EntityComparator<T> {
        boolean compare(T expected, T actual);
    }

    private static final EntityComparator DEFAULT_COMPARATOR =
            (Object expected, Object actual) -> String.valueOf(expected).equals(String.valueOf(actual));

    protected EntityComparator<T> entityComparator;
    protected Class<T> entityClass;

    public ModelMatcher(Class<T> entityClass) {
        this(entityClass, (EntityComparator<T>) DEFAULT_COMPARATOR);
    }

    public ModelMatcher(Class<T> entityClass, EntityComparator<T> entityComparator) {
        this.entityClass = entityClass;
        this.entityComparator = entityComparator;
    }

    private static class EntityWrapper<T> {
        private T entity;
        private EntityComparator<T> entityComparator;

        private EntityWrapper(T entity, EntityComparator<T> entityComparator) {
            this.entity = entity;
            this.entityComparator = entityComparator;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            EntityWrapper<T> that = (EntityWrapper<T>) o;
            return entity != null ? entityComparator.compare(entity, that.entity) : that.entity == null;
        }

        @Override
        public String toString() {
            return "EntityWrapper{" +
                    "entity=" + entity +
                    ", entityComparator=" + entityComparator +
                    '}';
        }
    }

    public void assertEquals(T expected, T actual) {
        Assert.assertEquals(new EntityWrapper<>(expected, entityComparator), new EntityWrapper<>(actual, entityComparator));
    }

    public void assertCollectionEquals(Collection<T> expected, Collection<T> actual) {
        Assert.assertEquals(map(expected), map(actual));
    }

    public List<EntityWrapper<T>> map(Collection<T> collection) {
        return collection.stream().map(e -> new EntityWrapper<>(e, entityComparator)).collect(Collectors.toList());
    }

}