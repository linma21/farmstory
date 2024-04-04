package kr.co.farmstory.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.farmstory.dto.MarketPageRequestDTO;
import kr.co.farmstory.dto.ProductDTO;
import kr.co.farmstory.entity.*;
import kr.co.farmstory.repository.custom.MarketRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MarketRepositoryImpl implements MarketRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QProduct qProduct = QProduct.product;
    private final QImages qImages = QImages.images;
    private final QCart qCart = QCart.cart;
    private final QCart_product qCart_product = QCart_product.cart_product;

    // 장보기 게시판 목록 출력 (market/list)
    @Override
    public Page<Product> selectProducts(MarketPageRequestDTO marketPageRequestDTO, Pageable pageable) {
        /*
            3가지로 분기 // 중복 코드 합치기
            1. 파라미터 하나도 없을 때
            2. cate만 있을 때
            3. type keyword 있을 때
        */
        List<Product> productList = new ArrayList<>();
        long total = 0;
        if ((marketPageRequestDTO.getCate()==null || marketPageRequestDTO.getCate().isEmpty()) && marketPageRequestDTO.getType()==null){
            // 1. 파라미터 없이 호출했을 경우 (상단 배너로 호출)
            // select * from `product` order by no desc limt (0, 10)
            productList = jpaQueryFactory
                    .selectFrom(qProduct)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(qProduct.prodno.desc())
                    .fetch();

            total = jpaQueryFactory.selectFrom(qProduct).fetchCount();
        }else if (marketPageRequestDTO.getCate()!=null && marketPageRequestDTO.getType()==null){
            // 2. cate만 있는 경우 (상품 분류로 호출)
            // select * from `product` where `cate`=? order by no desc limt (0, 10)
            productList = jpaQueryFactory
                    .selectFrom(qProduct)
                    .where(qProduct.cate.eq(marketPageRequestDTO.getCate()))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(qProduct.prodno.desc())
                    .fetch();

            total = jpaQueryFactory.selectFrom(qProduct).where(qProduct.cate.eq(marketPageRequestDTO.getCate())).fetchCount();
        }else {
            // 3. type keyword 있는 경우 (검색으로 호출)
            // select * from `product` where `type`= keyword order by no desc limt (0, 10)
        }

        return new PageImpl<>(productList, pageable, total);
    }

    // 장보기 게시판 게시글 출력 (market/view)
    @Override
    public List<Tuple> selectProduct(int prodno){
        // select * from `product` as a join `images` as b on a.prodno = b.prodno where a`prodno` = ?
        List<Tuple> joinProduct = jpaQueryFactory
                                        .select(qProduct, qImages)
                                        .from(qProduct)
                                        .join(qImages)
                                        .on(qProduct.prodno.eq(qImages.prodno))
                                        .where(qProduct.prodno.eq(prodno))
                                        .fetch();

        log.info("results : " + joinProduct);
        return joinProduct;

    };

    // 장바구니 목록 출력
    @Override
    public List<Tuple> selectCartForMarket(String uid){
        // select * from `cart` where uid = ?
        Integer cartNo = jpaQueryFactory
                                .select(qCart.cartNo)
                                .from(qCart)
                                .where(qCart.uid.eq(uid))
                                .fetchOne();
        log.info("selectCartForMarket1-cartNoList : " + cartNo);
        List<Tuple> productList = new ArrayList<>();
        // SELECT * FROM `cart_product` AS a  JOIN `product` AS b ON a.prodno = b.prodno WHERE `cartNo` = ?;
        if (cartNo != null){
            productList = jpaQueryFactory
                            .select(qCart_product.count, qProduct)
                            .from(qCart_product)
                            .join(qProduct)
                            .on(qCart_product.prodNo.eq(qProduct.prodno))
                            .where(qCart_product.cartNo.eq(cartNo))
                            .fetch();
        }
        log.info("selectCartForMarket2-productList : " + productList.toString());
        return productList;
    }
}
