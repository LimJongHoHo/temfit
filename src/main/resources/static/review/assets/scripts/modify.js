const $modifyForm = document.getElementById('modifyForm');
const contentRegex = new RegExp('^(.{1,100000})$');

$modifyForm.onsubmit = (e) => {
    e.preventDefault();

    if (editor.getData() === '') {
        dialog.showSimpleOk('리뷰 수정', '내용을 입력해 주세요 !', {
            onOkCallback: () => $modifyForm['content'].focus()
        });
        return;
    }
    if (!contentRegex.test($modifyForm['content'].value)) {
        dialog.showSimpleOk('리뷰 수정', '올바른 내용을 입력해 주세요.', {
            onOkCallback: () => $modifyForm['content'].focus()
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
            dialog.showSimpleOk('리뷰 수정', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure':
                dialog.showSimpleOk('리뷰 수정', '세션이 만료되었거나 게시글을 수정할 권한이 없습니다. 관리자에게 문의해 주세요.');
                break;
            case 'success':
                location.href = `/review/list?id=${response.id}`;
                break;
            default:
                dialog.showSimpleOk('리뷰 수정', '알 수 없는 이유로 게시글을 수정하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('PATCH', '/review/modify');
    xhr.send(formData);
}