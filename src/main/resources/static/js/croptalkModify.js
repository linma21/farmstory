// util.js 함수 호출
document.write('<script src="../js/util.js"></script>');
window.onload = function (){
    <!-- thymeleaf 변수를 js 파일에서 사용하기 위해 input value 가져오기 -->
    const cateData = document.getElementById("cate");
    const cate = cateData.value;
    const btnSubmit = document.getElementById('btnSubmit');
    const communityForm = document.getElementById('communityForm');

    const btnClose = document.getElementsByClassName('btn-close');
    const fileInput = document.getElementById('formFileMultiple');
    const fileValue = communityForm.file.value;

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
    // croptalk 상단 Nav 표시하기
    if(cate === 'story'){
        communityNav.innerHTML = `<img src="../images/sub_nav_tit_cate3_tit1.png" alt="농작물이야기"/>
                                                <h5>HOME > 농작물이야기 > <span>농작물이야기</span></h5>`;
    } else if(cate === 'grow'){
        communityNav.innerHTML = `<img src="../images/sub_nav_tit_cate3_tit2.png" alt="귀농학교"/>
                                                <h5>HOME > 농작물이야기 > <span>귀농학교</span></h5>`;
    } else if(cate === 'chef'){
        communityNav.innerHTML = `<img src="../images/sub_nav_tit_cate3_tit3.png" alt="텃밭가꾸기"/>
                                                <h5>HOME > 농작물이야기 > <span>텃밭가꾸기</span></h5>`;
    }
    // 페이지 로드시 textarea 자동 높이 조절
    const textareas = document.querySelectorAll('textarea');

    // 선택된 각 textarea 요소에 대해 autoResize 함수를 호출합니다.
    textareas.forEach(textarea => {
        autoResize(textarea);
    });
    // 첨부파일 삭제 - Delete는 아직 안함
    let deleteList = []; // 삭제할 파일 번호 저장해두는 배열
    for (const close of btnClose){
        close.onclick = function(){
            const result = confirm("첨부파일을 삭제하시겠습니까?");
            if(result){
                const li = this.parentElement;
                const fno = this.dataset.fno;
                console.log("fno : " + fno);
                // 삭제할 파일 리스트에 추가
                deleteList.push(fno);
                console.log("deleteList : " + deleteList);
                li.remove();
            }
        }
    }

    // 게시글 수정 하기 //////////////////////////////////////////////////////
    btnSubmit.onclick = function (e){
        e.preventDefault();
        if (confirm("수정된 사항을 저장하시겠습니까?")){
            console.log("기존 파일 수 : " + fileValue);
            console.log("삭제 파일 수 : " + deleteList.length);
            console.log("추가 파일 수 : " + fileInput.files.length);
            // 최종 파일 갯수
            const count = fileValue - deleteList.length + fileInput.files.length;
            console.log("최종 파일 수 : " + count);
            // 최종 파일 갯수 form에 입력
            communityForm.file.value = count;

            // 파일 삭제 먼저 하기
            if(deleteList != null){
                for (const fno of deleteList){
                    const data = fetchDelete(`/farmstory/fileDelete/${fno}`);
                }
            }
            // 폼 전송
            communityForm.submit();
        }
    }
}
// 텍스트 입력시 textarea 자동 높이 조절 - onload 밖에 둬야함
function autoResize(textarea) {
    // 텍스트 영역의 스크롤 높이 설정을 임시로 해제
    textarea.style.height = 'auto';

    // 텍스트 영역의 스크롤 높이를 내용에 맞게 조절
    textarea.style.height = textarea.scrollHeight + 'px';
}