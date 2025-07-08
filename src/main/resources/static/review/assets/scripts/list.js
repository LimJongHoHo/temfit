const $reviewList = document.querySelectorAll('#main > .page-container > .review-container > .review');


$reviewList.forEach(($review) => {
    const id = $review.querySelector(':scope > input').value;
    $review.querySelector(':scope > .review-caption > .---button-container > .--object-button.-color-pink.modify').addEventListener('click', () => {
        location.href = `/review/modify?id=${id}`;
    });

    $review.querySelector(':scope > .review-caption > .---button-container > .--object-button.-color-gray.delete').addEventListener('click', () => {
        dialog.show({
            title: '리뷰 삭제',
            content: '정말로 리뷰를 삭제할까요? 삭제된 리뷰는 복구가 어렵습니다.',
            buttons: [
                {
                    caption: '취소',
                    onclick: ($modal) => dialog.hide($modal)
                },
                {
                    caption: '삭제',
                    color: 'red',
                    onclick: ($modal) => {
                        dialog.hide($modal);
                        const xhr = new XMLHttpRequest();
                        const formData = new FormData();
                        formData.append('id', id);
                        xhr.onreadystatechange = () => {
                            if (xhr.readyState !== XMLHttpRequest.DONE) {
                                return;
                            }
                            if (xhr.status < 200 || xhr.status >= 300) {
                                dialog.showSimpleOk('리뷰 삭제', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
                                return;
                            }
                            const response = JSON.parse(xhr.responseText);
                            switch (response.result) {
                                case 'failure_absent':
                                    dialog.showSimpleOk('리뷰 삭제', '리뷰가 더 이상 존재하지 않습니다.')
                                    break;
                                case 'failure_session_expired':
                                    dialog.showSimpleOk('리뷰 삭제', '세션이 만료되었거나 리뷰를 삭제할 권한이 없습니다. 관리자에게 문의해 주세요.');
                                    break;
                                case 'success':
                                    dialog.showSimpleOk('리뷰 삭제', '리뷰를 성공적으로 삭제하였습니다.');
                                    location.href = `/review/list?articleId=${new URL(location.href).searchParams.get('articleId')}`;
                                    break;
                                default:
                                    dialog.showSimpleOk('리뷰 삭제', '알 수 없는 이유로 리뷰를 삭제하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                            }
                        };
                        xhr.open('DELETE', '/review/');
                        xhr.send(formData);
                    }
                }
            ]
        });
    })
});