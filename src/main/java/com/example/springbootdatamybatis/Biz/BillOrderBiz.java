package com.example.springbootdatamybatis.Biz;

import com.example.springbootdatamybatis.Iterables.Iterables2;
import com.example.springbootdatamybatis.bean.*;
import com.example.springbootdatamybatis.mapper.BillOrderDemoMapper;
import com.example.springbootdatamybatis.mapper.BillOrderMapper;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
public class BillOrderBiz {
    @Resource
    private BillOrderMapper billOrderMapper;

    @Resource
    private BillOrderDemoMapper billOrderDemoMapper;

    public BillOrderDemo getOne(Long id) {
        return  billOrderDemoMapper.selectByPrimaryKey(id);
    }


    public BillOrder queryAll() {
        Req req = Req.builder()
                .currentPage(1)
                .pageSize(10).build();
        Iterable<List<BillOrder>> iterable = test(req);

        iterable.forEach(ar -> {
            System.out.println("===========" + ar.size());
        });

        Req req1 = Req.builder()
                .currentPage(1)
                .pageSize(10).build();
        test2(req1).forEach(ar -> {
            System.out.println("===========>>>>" + ar.size());
        });

        Req req2 = Req.builder()
                .currentPage(1)
                .pageSize(10).build();
        test3(req2).forEach(ar -> {
            System.out.println("test3===========>>>>" + ar.size());
        });


        LimitReq limitReq = LimitReq.builder()
                .limit(new Limit(0, 10))
                .build();
        test4(limitReq).forEach(ar -> {
            System.out.println("test4===========>>>>" + ar.size());
        });


        return new BillOrder();
    }

    public Iterable<List<BillOrder>> test(Req req) {

        long count = billOrderMapper.count();
        return () -> new Iterator<List<BillOrder>>() {
            @Override
            public boolean hasNext() {
                return count - req.getOffset() > 0;
            }

            @Override
            public List<BillOrder> next() {
                List<BillOrder> typeInfoList = null;
                typeInfoList = billOrderMapper.queryByRequest(req);
                req.setCurrentPage(req.getCurrentPage() + 1);
                return Optional.ofNullable(typeInfoList).orElse(Lists.newArrayList());
            }
        };
    }

    public Iterable<List<BillOrder>> test2(Req req) {
        return () -> new AbstractIterator<List<BillOrder>>() {
            boolean end = false;

            @Override
            protected List<BillOrder> computeNext() {
                if (end) {
                    return endOfData();
                }
                List<BillOrder> typeInfoList = billOrderMapper.queryByRequest(req);
                if (typeInfoList.size() < req.getPageSize()) {
                    end = true;
                }
                if (CollectionUtils.isEmpty(typeInfoList)) {
                    return endOfData();
                }
                req.setCurrentPage(req.getCurrentPage() + 1);
                return Optional.ofNullable(typeInfoList).orElse(Lists.newArrayList());
            }
        };
    }


    public Iterable<List<BillOrder>> test3(Req req) {
        return Iterables2.split((start, limit) -> {
            req.setCurrentPage(start / limit + 1);
            req.setPageSize(limit);
            return billOrderMapper.queryByRequest(req);
        }, 10);
    }

    public Iterable<List<BillOrderVo>> test4(LimitReq req) {
        return Iterables2.split((start, limit) -> {
            req.setLimit(new Limit(start, limit));
            return billOrderMapper.queryByLimit(req);
        }, 10);
    }


}
