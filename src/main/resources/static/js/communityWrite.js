// util.js 함수 호출
document.write('<script src="../js/util.js"></script>');
window.onload = function (){
    <!-- thymeleaf 변수를 js 파일에서 사용하기 위해 input value 가져오기 -->
    const cateData = document.getElementById("cate");
    const cate = cateData.value;
    const btnSubmit = document.getElementById('btnSubmit');
    const articleForm = document.getElementById('articleForm');
    // 커뮤니티 공통 ///////////////////////////////////////////////////////////////////
    const cateLi = document.querySelectorAll(".lnb li");
    const communityNav = document.getElementById('communityNav');

    // aside 현재 카테고리 표시하기 - 반복처리
    cateLi.forEach(function(item) {

        const dataCate = item.getAttribute("data-cate");

        // 현재 cate와 li의 data-cate 값을 비교하여 일치하는 경우
        if (dataCate === cate) {
            // 해당 요소에 클래스 추가
            item.classList.add("tabOn");
        }
    });
    // community 상단 Nav 표시하기
    if(cate === 'notice'){
        communityNav.innerHTML = `<img src="../images/sub_nav_tit_cate5_tit1.png" alt="공지사항"/>
                                                <h5>HOME > 커뮤니티 > <span>공지사항</span></h5>`;
    } else if(cate === 'menu'){
        communityNav.innerHTML = `<img src="../images/sub_nav_tit_cate5_tit2.png" alt="오늘의식단"/>
                                                <h5>HOME > 커뮤니티 > <span>오늘의식단</span></h5>`;
    } else if(cate === 'chef'){
        communityNav.innerHTML = `<img src="../images/sub_nav_tit_cate5_tit3.png" alt="나도요리사"/>
                                                <h5>HOME > 커뮤니티 > <span>나도요리사</span></h5>`;
    } else if(cate === 'qna'){
        communityNav.innerHTML = `<img src="../images/sub_nav_tit_cate5_tit4.png" alt="1:1고객문의"/>
                                                <h5>HOME > 커뮤니티 > <span>1:1고객문의</span></h5>`;
    } else if(cate === 'faq'){
        communityNav.innerHTML = `<img src="../images/sub_nav_tit_cate5_tit5.png" alt="자주묻는질문"/>
                                                <h5>HOME > 커뮤니티 > <span>자주묻는질문</span></h5>`;
    }

    // 글쓰기 /////////////////////////////////////////////////////////
    btnSubmit.onclick = function (e){
        e.preventDefault();
        articleForm.submit();
    };
}