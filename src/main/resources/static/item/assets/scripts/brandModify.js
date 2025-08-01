const $brandModifyForm = document.getElementById('brandModifyForm');
const $image = $brandModifyForm.querySelector(':scope > .--object-label-row > .image');
const $realUpload = $brandModifyForm.querySelector(':scope > .real-upload');

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
                    if (response.error === undefined) {
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
    $image.setAttribute('src', url);
}

$image.addEventListener('click',() => $realUpload.click());
$realUpload.addEventListener('change', getImageFiles);

$brandModifyForm.onsubmit = (e) => {
    e.preventDefault();

    if ($brandModifyForm['brandName'].value === '') {
        dialog.showSimpleOk('브랜드등록 오류', '브랜드이름을 입력해주세요.');
        $brandModifyForm['brandName'].parentElement.setValid(false);
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('id', new URL(location.href).searchParams.get('id'));
    formData.append('imageUrl', $image.getAttribute('src'));
    formData.append('name', $brandModifyForm['brandName'].value);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('브랜드등록', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure':
                dialog.showSimpleOk('브랜드등록', '세션이 만료되었습니다. 관리자에게 문의해 주세요.');
                break;
            case 'success':
                dialog.showSimpleOk('브랜드등록', '브랜드등록을 성공하였습니다.', {
                    onOkCallback: () => $brandModifyForm.querySelector(':scope > .button-container > a').click()
                });
                break;
            default:
                dialog.showSimpleOk('브랜드등록', '알 수 없는 이유로 브랜드등록을 하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('POST', '/item/brand-modify');
    xhr.send(formData);
}