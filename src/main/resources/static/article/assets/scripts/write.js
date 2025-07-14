const $writeForm = document.getElementById('writeForm');
const titleRegex = new RegExp('^(.{1,100})$');
const contentRegex = new RegExp('^(.{1,100000})$');
const $coverContainer = $writeForm.querySelector(':scope > .cover-container');
const $coverAddButton = $coverContainer.querySelector(':scope > .add');
const $basicLabel = $coverContainer.querySelector(':scope > .--object-label.basic > input');


const coverLabelRemove = (e) => {
    const $coverLabel = e.currentTarget.parentNode;
    if ($coverLabel.classList.contains('basic')) {
        $coverLabel.setValid(false, '최소 하나의 이미지가 존재하여야 합니다.');
        return;
    }
    $coverLabel.remove();
}

const coverLabelOnFocusout = (e) => {
    const $coverLabel = e.currentTarget;
    const $coverImage = $coverLabel.querySelector(':scope > .image');
    const $coverUrl = $coverLabel.querySelector(':scope > input');
    $coverImage.setAttribute('src', "/assets/images/view.png");
    $coverLabel.setValid(true);
    if ($coverUrl.value === '') {
        $coverLabel.setValid(false, '커버 이미지 주소를 입력해 주세요.');
        return;
    }
    if (!$coverUrl.value.startsWith('http://') && !$coverUrl.value.startsWith('https://')) {
        $coverLabel.setValid(false, '올바른 이미지 주소를 입력해 주세요. 이미지 주소는 "http://" 혹은 "https://"로 시작하여야 합니다.');
        return;
    }
    $coverImage.setAttribute('src', $coverUrl.value);
    $coverImage.setVisible(true);
}

$coverContainer.querySelectorAll(':scope > .--object-label').forEach(($coverLabel) => {
    $coverLabel.querySelector(':scope > .delete').addEventListener('click', coverLabelRemove);
    $coverLabel.addEventListener('focusout', coverLabelOnFocusout);
});

$coverAddButton.addEventListener('click', () => {
    if ($coverContainer.querySelectorAll(':scope > .--object-label').length === 8) {
        dialog.showSimpleOk('커버 이미지 추가', '최대 8개까지만 추가가 가능합니다.');
    } else {
        $coverAddButton.insertAdjacentHTML('beforebegin', `
        <label class="--object-label">
            <img alt="" class="image" src="/assets/images/view.png">
            <span class="---caption">커버 이미지</span>
            <input autocomplete="off" class="--object-field ---field" maxlength="1000" minlength="1"
                   name="coverUrl"
                   placeholder="커버 이미지 주소" spellcheck="false" type="text">
            <span class="---warning">커버 이미지 주소를 입력해 주세요.</span>
            <button class="--object-button -color-red delete" type="button">삭제</button>
        </label>`);
        $coverContainer.querySelector('.--object-label:last-of-type').addEventListener('focusout', coverLabelOnFocusout);
        $coverContainer.querySelector('.--object-label:last-of-type > .delete').addEventListener('click', coverLabelRemove);
    }
});

$writeForm.onsubmit = (e) => {
    e.preventDefault();

    if ($writeForm['product'].value === 'none') {
        dialog.showSimpleOk('게시글 작성', '상품을 선택해주세요.', {
            onOkCallback: () => $writeForm['product'].focus()
        });
        return;
    }

    if ($writeForm['title'].value === '') {
        dialog.showSimpleOk('게시글 작성', '제목을 입력해 주세요.', {
            onOkCallback: () => $writeForm['title'].focus()
        });
        return;
    }
    if (!titleRegex.test($writeForm['title'].value)) {
        dialog.showSimpleOk('게시글 작성', '올바른 제목을 입력해 주세요.', {
            onOkCallback: () => $writeForm['title'].focus()
        });
        return;
    }
    if (editor.getData() === '') {
        dialog.showSimpleOk('게시글 작성', '내용을 입력해 주세요.', {
            onOkCallback: () => $writeForm['content'].focus()
        });
        return;
    }
    if (!contentRegex.test($writeForm['content'].value)) {
        dialog.showSimpleOk('게시글 작성', '올바른 내용을 입력해 주세요.', {
            onOkCallback: () => $writeForm['content'].focus()
        });
        return;
    }
    if ($basicLabel.value === '') {
        dialog.showSimpleOk('게시글 작성', '커버 이미지 주소를 입력해주세요');
        return;
    }
    if (!$basicLabel.value.startsWith('http://') && !$basicLabel.value.startsWith('https://')) {
        dialog.showSimpleOk('게시글 작성', '올바른 이미지 주소를 입력해 주세요. 이미지 주소는 "http://" 혹은 "https://"로 시작하여야 합니다.');
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    let i = 1;
    $coverContainer.querySelectorAll(':scope > .--object-label > input').forEach(($coverUrl) => {
        if ($coverUrl.value === '' || $coverUrl.value === null) {
            formData.append('coverUrl' + i, null);
        } else {
            formData.append('coverUrl' + i, $coverUrl.value);
        }
        i++;
    });
    formData.append('productId', $writeForm['product'].value);
    formData.append('title', $writeForm['title'].value);
    formData.append('content', editor.getData());
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('게시글 작성', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure_session_expired':
                dialog.showSimpleOk('게시글 작성', '세션이 만료되었거나 게시글을 작성할 권한이 없습니다. 관리자에게 문의해 주세요.');
                break;
            case 'success':
                location.href = `/article/?id=${response.id}`;
                break;
            default:
                dialog.showSimpleOk('게시글 작성', '알 수 없는 이유로 게시글을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('POST', '/article/');
    xhr.send(formData);
}