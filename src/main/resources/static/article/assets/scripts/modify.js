const $modifyForm = document.getElementById('modifyForm');
const titleRegex = new RegExp('^(.{1,100})$');
const contentRegex = new RegExp('^(.{1,100000})$');
const $coverContainer = $modifyForm.querySelector(':scope > .cover-container');
const $realUpload = $modifyForm.querySelector(':scope > .real-upload')
const $coverAddButton = $modifyForm.querySelector(':scope > .--object-button.-color-blue.add');
const $itemModifyButton = $modifyForm.querySelector(':scope > .button-container > .--object-button.-color-gray.modify');

function getImageFiles(e) {
    const files = e.currentTarget.files;

    // 파일 타입 검사
    [...files].forEach(file => {
        if (!file.type.match("image/.*")) {
            alert('이미지 파일만 업로드가 가능합니다.');
            return
        }

        // 파일 갯수 검사
        if ([...files].length < 7) {
            const reader = new FileReader();
            reader.onload = () => {
                const xhr = new XMLHttpRequest();
                const formData = new FormData();
                formData.append('upload', $realUpload.files[0]);
                xhr.onreadystatechange = () => {
                    if (xhr.readyState !== XMLHttpRequest.DONE) {
                        return;
                    }
                    if (xhr.status < 200 || xhr.status >= 300) {
                        dialog.showSimpleOk('이미지 등록', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
                        return;
                    }
                    const response = JSON.parse(xhr.responseText);
                    if (response.error === '') {
                        createElement(response.url);
                    } else {
                        dialog.showSimpleOk('이미지 등록', response.error);
                    }
                };
                xhr.open('POST', '/article/image');
                xhr.send(formData);
            };
            reader.readAsDataURL(file);
        }
    });
}

function createElement(url) {
    $modifyForm.querySelector(':scope > .cover-container').insertAdjacentHTML('beforeend', `
        <label class="--object-label">
            <img alt="" class="image" src=${url}>
            <span class="---warning"></span>
            <button class="--object-button -color-red delete" type="button">X</button>
        </label>`);
    $coverContainer.querySelector('.--object-label:last-of-type > .delete').addEventListener('click', coverLabelRemove);
}

const coverLabelRemove = (e) => {
    const $coverLabel = e.currentTarget.parentNode;
    if ($coverLabel.classList.contains('basic')) {
        $coverLabel.setValid(false, '최소 하나의 이미지가 존재하여야 합니다.');
        return;
    }
    $coverLabel.remove();
}

$coverAddButton.addEventListener('click', () => {
    if ($coverContainer.querySelectorAll(':scope > .--object-label').length === 8) {
        dialog.showSimpleOk('커버 이미지 추가', '최대 8개까지만 추가가 가능합니다.');
    } else {
        $realUpload.value = '';
        $realUpload.click();
        $realUpload.addEventListener('change', getImageFiles);
    }
});

$modifyForm.onsubmit = (e) => {
    e.preventDefault();

    if ($modifyForm['product'].value === 'none') {
        dialog.showSimpleOk('게시글 작성', '상품을 선택해주세요.', {
            onOkCallback: () => $modifyForm['product'].focus()
        });
        return;
    }
    if ($modifyForm['title'].value === '') {
        dialog.showSimpleOk('게시글 작성', '제목을 입력해 주세요.', {
            onOkCallback: () => $modifyForm['title'].focus()
        });
        return;
    }
    if (!titleRegex.test($modifyForm['title'].value)) {
        dialog.showSimpleOk('게시글 작성', '올바른 제목을 입력해 주세요.', {
            onOkCallback: () => $modifyForm['title'].focus()
        });
        return;
    }
    if (editor.getData() === '') {
        dialog.showSimpleOk('게시글 작성', '내용을 입력해 주세요.', {
            onOkCallback: () => $modifyForm['content'].focus()
        });
        return;
    }
    if (!contentRegex.test($modifyForm['content'].value)) {
        dialog.showSimpleOk('게시글 작성', '올바른 내용을 입력해 주세요.', {
            onOkCallback: () => $modifyForm['content'].focus()
        });
        return;
    }
    if ($coverContainer.childElementCount === 0) {
        dialog.showSimpleOk('게시글 작성', '커버 이미지를 최소하나는 추가해주세요.');
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    let i = 1;
    $coverContainer.querySelectorAll(':scope > .--object-label').forEach(($label) => {
        formData.append('coverUrl' + i, $label.querySelector(':scope > .image').getAttribute('src'));
        i++;
    });
    formData.append('id', $modifyForm['id'].value);
    formData.append('productId', $modifyForm['product'].value);
    formData.append('title', $modifyForm['title'].value);
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
                location.href = `/article/?id=${$modifyForm['id'].value}`;
                break;
            default:
                dialog.showSimpleOk('게시글 작성', '알 수 없는 이유로 게시글을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('PATCH', '/article/');
    xhr.send(formData);
}

$itemModifyButton.setAttribute('href', `/item/modify?productId=${$modifyForm['product'].value}`);
$coverContainer.querySelectorAll('.--object-label > .delete').forEach((label) => {
    label.addEventListener('click', coverLabelRemove);
});