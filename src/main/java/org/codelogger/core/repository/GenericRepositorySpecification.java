package org.codelogger.core.repository;

import static java.lang.String.format;
import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.codelogger.utils.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

public class GenericRepositorySpecification {

  private static final String DECOLLATOR = "\\.";

  public static <T> Specification<T> withEqual(final String fieldPath, final Object fieldValue) {

    return new Specification<T>() {

      @Override
      public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query,
        final CriteriaBuilder builder) {

        String[] sequencePaths = fieldPath.split(DECOLLATOR);
        Path<String> path = null;
        for (String sequencePath : sequencePaths) {

          path = path == null ? root.<String> get(sequencePath) : path.<String> get(sequencePath);
        }
        return builder.equal(path, fieldValue);
      }
    };
  }

  public static <T, V extends Comparable<? super V>> Specification<T> withGreaterThan(
    final String fieldPath, final V fieldValue) {

    return new Specification<T>() {

      @SuppressWarnings("unchecked")
      @Override
      public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query,
        final CriteriaBuilder cb) {

        String[] sequencePaths = fieldPath.split(DECOLLATOR);
        Path<String> path = null;
        for (String sequencePath : sequencePaths) {

          path = path == null ? root.<String> get(sequencePath) : path.<String> get(sequencePath);
        }
        return cb.greaterThan(path.as(fieldValue.getClass()), fieldValue);
      }
    };
  }

  public static <T, V extends Comparable<? super V>> Specification<T> withLessThan(
    final String fieldPath, final V fieldValue) {

    return new Specification<T>() {

      @SuppressWarnings("unchecked")
      @Override
      public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query,
        final CriteriaBuilder cb) {

        String[] sequencePaths = fieldPath.split(DECOLLATOR);
        Path<String> path = null;
        for (String sequencePath : sequencePaths) {

          path = path == null ? root.<String> get(sequencePath) : path.<String> get(sequencePath);
        }
        return cb.lessThan(path.as(fieldValue.getClass()), fieldValue);
      }
    };
  }

  public static <T, V extends Comparable<? super V>> Specification<T> withBetween(
    final String fieldPath, final V lesserValue, final V greaterValue) {

    return new Specification<T>() {

      @SuppressWarnings("unchecked")
      @Override
      public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query,
        final CriteriaBuilder cb) {

        String[] sequencePaths = fieldPath.split(DECOLLATOR);
        Path<String> path = null;
        for (String sequencePath : sequencePaths) {

          path = path == null ? root.<String> get(sequencePath) : path.<String> get(sequencePath);
        }
        return cb.between(path.as(lesserValue.getClass()), lesserValue, greaterValue);
      }
    };
  }

  public static <T, V extends Comparable<? super V>> Specification<T> startWith(
    final String fieldPath, final V fieldValue) {

    return new Specification<T>() {

      @Override
      public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query,
        final CriteriaBuilder builder) {

        String[] sequencePaths = fieldPath.split(DECOLLATOR);
        Path<String> path = null;
        for (String sequencePath : sequencePaths) {

          path = path == null ? root.<String> get(sequencePath) : path.<String> get(sequencePath);
        }
        return builder.like(path, format("%s%%", fieldValue));
      }
    };
  }

  public static <T, V extends Comparable<? super V>> Specification<T> endWith(
    final String fieldPath, final V fieldValue) {

    return new Specification<T>() {

      @Override
      public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query,
        final CriteriaBuilder builder) {

        String[] sequencePaths = fieldPath.split(DECOLLATOR);
        Path<String> path = null;
        for (String sequencePath : sequencePaths) {

          path = path == null ? root.<String> get(sequencePath) : path.<String> get(sequencePath);
        }
        return builder.like(path, format("%%%s", fieldValue));
      }
    };
  }

  public static <T, V extends Comparable<? super V>> Specification<T> likeAnyWhere(
    final String fieldPath, final V fieldValue) {

    return new Specification<T>() {

      @Override
      public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query,
        final CriteriaBuilder builder) {

        String[] sequencePaths = fieldPath.split(DECOLLATOR);
        Path<String> path = null;
        for (String sequencePath : sequencePaths) {

          path = path == null ? root.<String> get(sequencePath) : path.<String> get(sequencePath);
        }
        return builder.like(path, format("%%%s%%", fieldValue));
      }
    };
  }

  public static <T> Specification<T> with(final String fieldPath, final Collection<?> fieldValues) {

    return new Specification<T>() {

      @Override
      public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query,
        final CriteriaBuilder builder) {

        String[] sequencePaths = fieldPath.split(DECOLLATOR);
        Path<String> path = null;
        for (String sequencePath : sequencePaths) {

          path = path == null ? root.<String> get(sequencePath) : path.<String> get(sequencePath);
        }
        Expression<Set<String>> jobStatus = root.get(sequencePaths[sequencePaths.length - 1]);
        return builder.isMember(CollectionUtils.join(fieldValues, ","), jobStatus);
      }
    };
  }

  /**
   * Simple static factory method to add some syntactic sugar around given
   * {@link org.springframework.data.jpa.domain.Specification}s with ANDs after
   * WHERE.
   *
   * @param specifications can be null or empty.
   * @return null if given specifications is null or empty, otherwise return
   *         sugar around
   *         {@link org.springframework.data.jpa.domain.Specification}.
   */
  public static <T> Specifications<T> toWhereSpecificationsJoinWithAnd(
    final List<Specification<T>> specifications) {

    if (CollectionUtils.isEmpty(specifications)) {
      return null;
    } else {
      Specifications<T> specs = where(specifications.remove(0));
      for (Specification<T> s : specifications) {
        specs = specs.and(s);
      }
      return specs;
    }
  }

  /**
   * Simple static factory method to add some syntactic sugar around given
   * {@link org.springframework.data.jpa.domain.Specification}s with ORs after
   * WHERE.
   *
   * @param specifications can be null or empty.
   * @return null if given specifications is null or empty, otherwise return
   *         sugar around
   *         {@link org.springframework.data.jpa.domain.Specification}.
   */
  public static <T> Specifications<T> toWhereSpecificationsJoinWithOr(
    final List<Specification<T>> specifications) {

    if (CollectionUtils.isEmpty(specifications)) {
      return null;
    } else {
      Specifications<T> specs = where(specifications.remove(0));
      for (Specification<T> s : specifications) {
        specs = specs.or(s);
      }
      return specs;
    }
  }
}