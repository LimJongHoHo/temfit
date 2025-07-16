const $buttonForm = document.querySelector('#container > .button-container > .--object-button.cart.-flex-stretch');
const $informationReview = document.querySelector('#main > .product-wrapper >.information-review');
const $information = $informationReview.querySelector(':scope > .two-container > .content.information');
const $review = $informationReview.querySelector(':scope > .two-container > .content.review');
const $informationSelect = $informationReview.querySelector(':scope > .information-page');
const $reviewSelect = $informationReview.querySelector(':scope > .review-container');
const $reviewAllSelect = $informationReview.querySelector(':scope > .review-container > .button-container');
const $delete = document.querySelector('#main > .product-wrapper > .modify-delete > .--object-button.-color-red.delete');

$review.addEventListener('click', () => {
    $review.setSelected(true);
    $information.setSelected(false);
    $reviewSelect.setSelected(true);
    $informationSelect.setSelected(false);

});

$information.addEventListener('click', () => {
    $review.setSelected(false);
    $information.setSelected(true);
    $reviewSelect.setSelected(false);
    $informationSelect.setSelected(true);
});

$reviewAllSelect.addEventListener('click', () => {
    const articleId = new URL(location.href).searchParams.get('id');
    location.href = `/review/list?articleId=${articleId}`;
});

$delete.addEventListener('click', () => {
    dialog.show({
        title: '게시글 삭제',
        content: `현재 게시글을 삭제할까요?`,
        buttons: [
            {
                caption: '아니요', onclick: ($modal) => {
                    dialog.hide($modal)
                }
            },
            {
                caption: '네',
                color: 'signature',
                onclick: ($modal) => {
                    dialog.hide($modal);
                    const url = new URL(location.href);
                    const id = url.searchParams.get('id');

                    const xhr = new XMLHttpRequest();
                    const formData = new FormData();
                    formData.append('id', id);
                    xhr.onreadystatechange = () => {
                        if (xhr.readyState !== XMLHttpRequest.DONE) {
                            return;
                        }
                        if (xhr.status < 200 || xhr.status >= 300) {
                            dialog.showSimpleOk('게시글 삭제', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
                            return;
                        }
                        const response = JSON.parse(xhr.responseText);
                        switch (response.result) {
                            case 'failure_absent':
                                dialog.showSimpleOk('게시글 삭제', '게시글이 더 이상 존재하지 않습니다.')
                                break;
                            case 'failure_session_expired':
                                dialog.showSimpleOk('게시글 삭제', '세션이 만료되었거나 게시글을 삭제할 권한이 없습니다. 관리자에게 문의해 주세요.');
                                break;
                            case 'success':
                                dialog.showSimpleOk('게시글 삭제', '게시글을 성공적으로 삭제하였습니다.');
                                break;
                            default:
                                dialog.showSimpleOk('게시글 삭제', '알 수 없는 이유로 게시글을 삭제하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                        }
                    };
                    xhr.open('DELETE', '/article/');
                    xhr.send(formData);
                }
            }
        ]
    });
});

const $itemCover = document.getElementById('itemCover');
const $item = document.getElementById('item');

$buttonForm.addEventListener('click', () => {
    $itemCover.classList.add('visible');
    $item.classList.add('visible');
});




const hidePay = () => {
    $itemCover.classList.remove('visible');
    $item.classList.remove('visible');
}

$item.querySelector(':scope > .button-container > .--object-button.cart').addEventListener('click', hidePay);






