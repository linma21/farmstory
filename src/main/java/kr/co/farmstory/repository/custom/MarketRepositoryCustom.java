package kr.co.farmstory.repository.custom;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import kr.co.farmstory.dto.MarketPageRequestDTO;
import kr.co.farmstory.dto.OrderDetailDTO;
import kr.co.farmstory.dto.OrderListResponseDTO;
import kr.co.farmstory.dto.PageRequestDTO;
import kr.co.farmstory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MarketRepositoryCustom {

    // market/list 페이지 products 조회
    public Page<Product> selectProducts(MarketPageRequestDTO marketPageRequestDTO, Pageable pageable);
    // market/view 페이지 product 조회
    public List<Tuple> selectProduct(int prodno);

    public List<Tuple> findOrderDetailsWithProductNameByUserId(String userId);

    // admin/order/list 페이지 조회
    public Page<Tuple> orderList(PageRequestDTO pageRequestDTO, Pageable pageable);

    // market/cart 페이지 cart_product 조회
    public List<Tuple> selectCartForMarket(String uid);

    // market/cart 페이지에서 market/order 넘어가면서 장바구니 count 변경
    public boolean modifyCount(int[] cart_prodNos, int[] counts);

    // market/cart 페이지에서 선택 상품 cart_prodNo 테이블에서 삭제
    public boolean deleteCart(int[] cart_prodNos);

    // main 페이지에서 띄울 상품 16개
    public List<Tuple> selectProductsForMain(String cate);
}