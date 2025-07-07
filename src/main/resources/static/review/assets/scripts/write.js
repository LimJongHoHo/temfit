const $writeForm = document.getElementById('writeForm');
const contentRegex = new RegExp('^(.{1,100000})$');

$writeForm.onsubmit = (e) => {
    e.preventDefault();

    if (editor.getData() === '') {
        dialog.showSimpleOk('리뷰 작성', '내용을 입력해 주세요 !', {
            onOkCallback: () => $writeForm['content'].focus()
        });
        return;
    }
    if (!contentRegex.test($writeForm['content'].value)) {
        dialog.showSimpleOk('리뷰 작성', '올바른 내용을 입력해 주세요.', {
            onOkCallback: () => $writeForm['content'].focus()
        });
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('content', editor.getData());
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('리뷰 작성', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure_session_expired':
                dialog.showSimpleOk('리뷰 작성', '세션이 만료되었거나 게시글을 작성할 권한이 없습니다. 관리자에게 문의해 주세요.');
                break;
            case 'success':
                location.href = `/review?id=${response.id}`;
                break;
            default:
                dialog.showSimpleOk('리뷰 작성', '알 수 없는 이유로 게시글을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('POST', '/review/');
    xhr.send(formData);
}