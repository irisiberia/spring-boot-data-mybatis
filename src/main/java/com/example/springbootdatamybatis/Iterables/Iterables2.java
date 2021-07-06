package com.example.springbootdatamybatis.Iterables;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Iterable工具类，通过此类可以屏蔽分批次查询处理逻辑。<br/>
 */
public class Iterables2 {

    /**
     * 将双层嵌套循环的转成一层循环
     */
    public static <T> Iterable<T> flatten(final Iterable<? extends Iterable<T>> iterables) {
        Preconditions.checkNotNull(iterables);
        return flatten(iterables.iterator());
    }

    public static <T> Iterable<T> flatten(final Iterator<? extends Iterable<T>> iterators) {
        Preconditions.checkNotNull(iterators);
        return () -> new AbstractIterator<T>() {
            Iterator<? extends Iterable<T>> outerIterator = iterators;
            Iterator<T> innerIterator = null;

            @Override
            protected T computeNext() {
                if (innerIterator == null) {
                    if (!outerIterator.hasNext()) {
                        return endOfData();
                    }
                    innerIterator = outerIterator.next().iterator();
                }
                while (innerIterator.hasNext()) {
                    return innerIterator.next();
                }
                innerIterator = null;
                return computeNext();
            }
        };
    }

    public static <T> Iterable<List<T>> split(final SimpleStartLimitQueryTemplate<T> queryTemplate, final int limit) {
        Preconditions.checkNotNull(queryTemplate);
        Preconditions.checkArgument(limit > 0);
        return () -> new AbstractIterator<List<T>>() {
            int start = 0;
            boolean reachEnd = false;

            @Override
            protected List<T> computeNext() {
                if (reachEnd) {
                    return endOfData();
                }
                List<T> result = queryTemplate.query(start, limit);
                if (result.size() < limit) {
                    reachEnd = true;
                }
                if (result.isEmpty()) {
                    return endOfData();
                } else {
                    start += limit;
                    return result;
                }
            }
        };
    }

    public   <T> List<T> split2List(final SimpleStartLimitQueryTemplate<T> queryTemplate, final int limit) {
        Preconditions.checkNotNull(queryTemplate);
        Preconditions.checkArgument(limit > 0);
        Iterable<List<T>> iterable = () -> new AbstractIterator<List<T>>() {
            int start = 0;
            boolean reachEnd = false;

            @Override
            protected List<T> computeNext() {
                if (reachEnd) {
                    return endOfData();
                }
                List<T> result = queryTemplate.query(start, limit);
                if (result.size() < limit) {
                    reachEnd = true;
                }
                if (result.isEmpty()) {
                    return endOfData();
                } else {
                    start += limit;
                    return result;
                }
            }
        };
        return Lists.newArrayList(iterable).stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * 将迭代器中数据分批次返回给客户端，以便客户端可以部分处理
     *
     * @param data 数据源
     * @param limit 条数限制
     * @param <T> 返回类型
     * @return 可以分批次处理的迭代器。
     */
    public static <T> Iterable<List<T>> split(final Iterable<T> data, final int limit) {
        return split(data.iterator(), limit);
    }

    /**
     * 将迭代器中数据分批次返回给客户端，以便客户端可以部分处理
     *
     * @param data 数据源
     * @param limit 条数限制
     * @param <T> 返回类型
     * @return 可以分批次处理的迭代器。
     */
    public static <T> Iterable<List<T>> split(final Iterator<T> data, final int limit) {
        return () -> new AbstractIterator<List<T>>() {
            boolean reachEnd = false;

            @Override
            protected List<T> computeNext() {
                if (reachEnd) {
                    return endOfData();
                }
                List<T> list = Lists.newArrayList();
                while (true) {
                    if (list.size() == limit) {
                        break;
                    }
                    if (!data.hasNext()) {
                        reachEnd = true;
                        break;
                    }
                    list.add(data.next());
                }
                if (list.isEmpty()) {
                    return endOfData();
                } else {
                    return list;
                }
            }
        };
    }

    public static <T> Iterable<List<T>> splitByPage(final SimplePageQueryTemplate<T> queryTemplate, final int pageSize) {
        Preconditions.checkNotNull(queryTemplate);
        Preconditions.checkArgument(pageSize > 0);
        return () -> new AbstractIterator<List<T>>() {
            int pageNo = 1;
            boolean reachEnd = false;

            @Override
            protected List<T> computeNext() {
                if (reachEnd) {
                    return endOfData();
                }
                List<T> result = queryTemplate.query(pageNo, pageSize);
                if (result.size() < pageSize) {
                    reachEnd = true;
                }
                if (result.isEmpty()) {
                    return endOfData();
                } else {
                    pageNo++;
                    return result;
                }
            }
        };
    }

}