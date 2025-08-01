const apiKey = '8GSZ7c0dLQEYPwbidlWLtFSLRW%2BXAUJbC28YAM0ZLTlpaF57Uru1YfFBjuTlLcq1iBnkP7iSFzeQoqCHTuchLA%3D%3D';
const $loading = document.getElementById('loading');
const $itemWriteForm = document.getElementById('itemWriteForm');
const $brand = document.getElementById('brand');
const $skin = document.getElementById('skin');
const $ingredientLabel = $itemWriteForm.querySelector(':scope > .ingredient-wrapper > .--object-label-row');
const $labels = $itemWriteForm.querySelectorAll(':scope > .--object-label-row')
const $image = $itemWriteForm.querySelector(':scope > .--object-label-row > .image');
const $ingredient = $itemWriteForm.querySelector(':scope > .ingredient-wrapper > .--object-label-row > .--object-field.---field.-flex-stretch.ingredient');
const $ingredientInfo = $itemWriteForm.querySelector(':scope > .ingredient-wrapper > .ingredient-information');
const $ingredientContainer = $itemWriteForm.querySelector(':scope > .ingredient-wrapper > .ingredient-container');
const $realUpload = $itemWriteForm.querySelector(':scope > .real-upload');
const $brandModifyButton = $itemWriteForm.querySelector(':scope > .--object-label-row > .button-container > .--object-button.-color-gray.modify');

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

$image.addEventListener('click', () => $realUpload.click());
$realUpload.addEventListener('change', getImageFiles);


function ingredientAdd(text, score) {
    document.querySelector('#itemWriteForm > .ingredient-wrapper > .ingredient-container').insertAdjacentHTML('beforeend', `
        <div class="item">
            <button class="delete" type="button">X</button>
            <span>(</span>
            <span class="score">${score}</span>
            <span>)</span>
            <span class="korName">${text}</span>
        </div>`);
}

function ingredientRemoveButton() {
    document.querySelectorAll('#itemWriteForm > .ingredient-wrapper > .ingredient-container > .item').forEach((item) => {
        item.querySelector(':scope > .delete').addEventListener('click', (button) => button.currentTarget.parentElement.remove());
    })
}

$itemWriteForm.onsubmit = (e) => {
    e.preventDefault();

    $labels.forEach(($label) => $label.setValid(true));
    if ($realUpload.value === '') {
        dialog.showSimpleOk('상품등록 오류', '커버 이미지를 선택해주세요.');
        $image.parentElement.setValid(false);
        return;
    }
    if ($itemWriteForm['productName'].value === '') {
        dialog.showSimpleOk('상품등록 오류', '상품이름을 입력해주세요.');
        $itemWriteForm['productName'].parentElement.setValid(false);
        return;
    }
    if ($brand.value === 'none') {
        dialog.showSimpleOk('상품등록 오류', '브랜드를 선택해주세요');
        $brand.parentElement.setValid(false);
        return;
    }
    if ($skin.value === 'none') {
        dialog.showSimpleOk('상품등록 오류', '브랜드를 선택해주세요');
        $skin.parentElement.setValid(false);
        return;
    }
    if ($ingredientContainer.childElementCount === 0) {
        dialog.showSimpleOk('상품등록 오류', '성분을 등록해주세요');
        $ingredientLabel.setValid(false);
        return;
    }
    if ($itemWriteForm['size'].value === '') {
        dialog.showSimpleOk('상품등록 오류', '용량을 입력해주세요.');
        $itemWriteForm['size'].parentElement.setValid(false);
        return;
    }
    if ($itemWriteForm['price'].value === '') {
        dialog.showSimpleOk('상품등록 오류', '가격을 입력해주세요');
        $itemWriteForm['price'].parentElement.setValid(false);
        return;
    }
    if ($itemWriteForm['price'].value < 0) {
        dialog.showSimpleOk('상품등록 오류', '올바른 가격을 입력해주세요');
        $itemWriteForm['price'].parentElement.setValid(false);
        return;
    }
    if ($itemWriteForm['rate'].value === '') {
        dialog.showSimpleOk('상품등록 오류', '할인율을 입력해주세요');
        $itemWriteForm['rate'].parentElement.setValid(false);
        return;
    }
    if ($itemWriteForm['rate'].value < 0 || $itemWriteForm['rate'].value > 100) {
        dialog.showSimpleOk('상품등록 오류', '올바른 할인율을 입력해주세요');
        $itemWriteForm['rate'].parentElement.setValid(false);
        return;
    }
    if ($itemWriteForm['deliveryFee'].value === '') {
        dialog.showSimpleOk('상품등록 오류', '배송비를 입력해주세요');
        $itemWriteForm['deliveryFee'].parentElement.setValid(false);
        return;
    }
    if ($itemWriteForm['deliveryFee'].value < 0) {
        dialog.showSimpleOk('상품등록 오류', '올바른 배송비를 입력해주세요');
        $itemWriteForm['deliveryFee'].parentElement.setValid(false);
        return;
    }
    if ($itemWriteForm['deliveryCompany'].value === '') {
        dialog.showSimpleOk('상품등록 오류', '배송사를 입력해주세요');
        $itemWriteForm['deliveryCompany'].parentElement.setValid(false);
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('imageUrl', $image.getAttribute('src'));
    formData.append('name', $itemWriteForm['productName'].value);
    formData.append('brandId', $brand.value);
    formData.append('skinId', $skin.value);
    formData.append('size', $itemWriteForm['size'].value);
    formData.append('price', $itemWriteForm['price'].value);
    formData.append('deliveryFee', $itemWriteForm['deliveryFee'].value);
    formData.append('deliveryCompany', $itemWriteForm['deliveryCompany'].value);
    formData.append('deliveryAdd', $itemWriteForm['deliveryAdd'].value);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('상품등록', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure':
                dialog.showSimpleOk('상품등록', '세션이 만료되었습니다. 관리자에게 문의해 주세요.');
                break;
            case 'success':
                $ingredientContainer.querySelectorAll(':scope > .item').forEach((ingredient) => {
                    const xhr2 = new XMLHttpRequest();
                    const formData2 = new FormData();
                    formData2.append('korName', ingredient.querySelector(':scope > .korName').innerText);
                    formData2.append('score', ingredient.querySelector(':scope > .score').innerText);
                    formData2.append('productId', response.productId);
                    xhr2.onreadystatechange = () => {
                        if (xhr2.readyState !== XMLHttpRequest.DONE) {
                            return;
                        }
                        if (xhr2.status < 200 || xhr2.status >= 300) {
                            dialog.showSimpleOk('성분추가', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
                            return;
                        }
                        const response2 = JSON.parse(xhr.responseText);
                        switch (response2.result) {
                            case 'failure':
                                dialog.showSimpleOk('상품등록', '세션이 만료되었습니다. 관리자에게 문의해 주세요.');
                                break;
                            case 'success':
                                break;
                            default:
                                dialog.showSimpleOk('상품등록', '알 수 없는 이유로 성분을 등록 하지 못하였습니다.');
                        }
                    };
                    xhr2.open('POST', '/item/ingredient');
                    xhr2.send(formData2);
                });

                dialog.showSimpleOk('상품등록', '상품등록을 성공하였습니다.', {
                    onOkCallback: () => $itemWriteForm.querySelector(':scope > .button-container > a').click()
                });
                break;
            default:
                dialog.showSimpleOk('상품등록', '알 수 없는 이유로 상품등록을 하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('POST', '/item/');
    xhr.send(formData);
}

$ingredient.addEventListener('keyup', () => {
    if ($itemWriteForm['ingredient'].value === '') {
        $ingredientInfo.setVisible(false);
    } else {
        $ingredientInfo.setVisible(true);
    }
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 400) {
            alert(`요청을 전송하는 도중 오류가 발생하였습니다. (${xhr.status})`);
            return;
        }
        const response = JSON.parse(xhr.responseText);
        const ingredients = response['body']['items'];
        const $infoLabel = $ingredientInfo;
        $infoLabel.innerHTML = '';
        let infoHtml = ``
        if (ingredients === undefined) {
            $infoLabel.innerHTML = `
                <div class="empty">
                    <img src="/assets/images/cart-warning.png" alt="">
                    <span class="caption">검색결과 없습니다.</span>
                </div>`;
        } else {
            infoHtml = `
            <div class="container">
                <span class="---caption">표준명</span>
                <span class="---caption">영문명</span>
                <span class="---caption">CASno</span>
                <span class="---caption">기원및정의</span>
                <span class="---caption">이명</span>
            </div>`;
            for (const ingredient of ingredients) {
                const itemHtml = `
                <button class="item" type="button">
                    <span class="korName">${ingredient['INGR_KOR_NAME'] == null ? '-' : ingredient['INGR_KOR_NAME']}</span>
                    <span class="engName">${ingredient['INGR_ENG_NAME'] == null ? '-' : ingredient['INGR_ENG_NAME']}</span>
                    <span class="casNo">${ingredient['CAS_NO'] == null ? '-' : ingredient['CAS_NO']}</span>
                    <span class="originName">${ingredient['ORIGIN_MAJOR_KOR_NAME'] == null ? '-' : ingredient['ORIGIN_MAJOR_KOR_NAME']}</span>
                    <span class="synonym">${ingredient['INGR_SYNONYM'] == null ? '-' : ingredient['INGR_SYNONYM']}</span>
                </button>`;
                infoHtml += itemHtml;
            }

            $infoLabel.innerHTML = infoHtml;

            $ingredientInfo.querySelectorAll(':scope > .item').forEach(($item) => {
                $item.addEventListener('click', () => {
                    const xhr = new XMLHttpRequest();
                    const formData = new FormData();
                    formData.append('engName', $item.querySelector(':scope > .engName').innerText);
                    xhr.onreadystatechange = () => {
                        if (xhr.readyState !== XMLHttpRequest.DONE) {
                            return;
                        }
                        $loading.hide();
                        if (xhr.status < 200 || xhr.status >= 300) {
                            dialog.showSimpleOk('성분추가', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
                            return;
                        }
                        let score;
                        const response = JSON.parse(xhr.responseText);
                        if (response.score === 'X') {
                            score = '미분류';
                        } else {
                            score = response.score;
                        }

                        ingredientAdd($item.querySelector(':scope > .korName').innerText, score);
                        ingredientRemoveButton();
                        $ingredientInfo.setVisible(false);
                    };
                    xhr.open('POST', '/item/waring-score');
                    xhr.send(formData);
                    $loading.show();
                });
            });
        }
    };
    xhr.open('GET', `https://apis.data.go.kr/1471000/CsmtcsIngdCpntInfoService01/getCsmtcsIngdCpntInfoService01?serviceKey=${apiKey}&pageNo=1&numOfRows=100&type=json&INGR_KOR_NAME=${$ingredient.value}`);
    xhr.send();
});

$brand.addEventListener('focusout', () => {
    $brandModifyButton.setAttribute('href', `/item/brand-modify?id=${$brand.value}`)
});