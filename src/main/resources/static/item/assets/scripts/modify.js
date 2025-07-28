const apiKey = '8GSZ7c0dLQEYPwbidlWLtFSLRW%2BXAUJbC28YAM0ZLTlpaF57Uru1YfFBjuTlLcq1iBnkP7iSFzeQoqCHTuchLA%3D%3D';
const $loading = document.getElementById('loading');
const $itemModifyForm = document.getElementById('itemModifyForm');
const $brand = document.getElementById('brand');
const $skin = document.getElementById('skin');
const $ingredientLabel = $itemModifyForm.querySelector(':scope > .ingredient-wrapper > .--object-label-row');
const $labels = $itemModifyForm.querySelectorAll(':scope > .--object-label-row')
const $image = $itemModifyForm.querySelector(':scope > .--object-label-row > .image');
const $ingredient = $itemModifyForm.querySelector(':scope > .ingredient-wrapper > .--object-label-row > .--object-field.---field.-flex-stretch.ingredient');
const $ingredientInfo = $itemModifyForm.querySelector(':scope > .ingredient-wrapper > .ingredient-information');
const $ingredientContainer = $itemModifyForm.querySelector(':scope > .ingredient-wrapper > .ingredient-container');

$itemModifyForm['productImage'].addEventListener('focusout', () => {
    $itemModifyForm['productImage'].parentElement.setValid(true)
    if ($itemModifyForm['productImage'].value === '') {
        $itemModifyForm['productImage'].parentElement.setValid(false, '커버 이미지 주소를 입력해 주세요.');
        return;
    }
    if (!$itemModifyForm['productImage'].value.startsWith('http://') && !$itemModifyForm['productImage'].value.startsWith('https://')) {
        $itemModifyForm['productImage'].parentElement.setValid(false, '이미지 주소는 "http://" 혹은 "https://"로 시작하여야 합니다.');
        return;
    }
    $image.setAttribute('src', $itemModifyForm['productImage'].value);
    $itemModifyForm['productImage'].parentElement.setValid(true);
});

function ingredientAdd(text, score) {
    document.querySelector('#itemModifyForm > .ingredient-wrapper > .ingredient-container').insertAdjacentHTML('beforeend', `
        <div class="item">
            <button class="delete" type="button">X</button>
            <span>(</span>
            <span class="score">${score}</span>
            <span>)</span>
            <span class="korName">${text}</span>
        </div>`);
}

function ingredientRemoveButton() {
    document.querySelectorAll('#itemModifyForm > .ingredient-wrapper > .ingredient-container > .item').forEach((item) => {
        item.querySelector(':scope > .delete').addEventListener('click', (button) => button.currentTarget.parentElement.remove());
    })
}

$itemModifyForm.onsubmit = (e) => {
    e.preventDefault();

    $labels.forEach(($label) => $label.setValid(true));
    if ($itemModifyForm['productImage'].value === '') {
        dialog.showSimpleOk('상품수정 오류', '커버 이미지 주소를 입력해주세요.');
        $itemModifyForm['productImage'].parentElement.setValid(false, '커버 이미지 주소를 입력해 주세요.');
        return;
    }
    if (!$itemModifyForm['productImage'].value.startsWith('http://') && !$itemModifyForm['productImage'].value.startsWith('https://')) {
        dialog.showSimpleOk('상품수정 오류', '이미지 주소는 "http://" 혹은 "https://"로 시작하여야 합니다.');
        $itemModifyForm['productImage'].parentElement.setValid(false, '이미지 주소는 "http://" 혹은 "https://"로 시작하여야 합니다.');
        return;
    }
    if ($itemModifyForm['productName'].value === '') {
        dialog.showSimpleOk('상품수정 오류', '상품이름을 입력해주세요.');
        $itemModifyForm['productName'].parentElement.setValid(false);
        return;
    }
    if ($brand.value === 'none') {
        dialog.showSimpleOk('상품수정 오류', '브랜드를 선택해주세요');
        $brand.parentElement.setValid(false);
        return;
    }
    if ($ingredientContainer.childElementCount === 0) {
        dialog.showSimpleOk('상품수정 오류', '성분을 등록해주세요');
        $ingredientLabel.setValid(false);
        return;
    }
    if ($itemModifyForm['size'].value === '') {
        dialog.showSimpleOk('상품수정 오류', '용량을 입력해주세요.');
        $itemModifyForm['size'].parentElement.setValid(false);
        return;
    }
    if ($itemModifyForm['price'].value === '') {
        dialog.showSimpleOk('상품수정 오류', '가격을 입력해주세요');
        $itemModifyForm['price'].parentElement.setValid(false);
        return;
    }
    if ($itemModifyForm['price'].value < 0) {
        dialog.showSimpleOk('상품수정 오류', '올바른 가격을 입력해주세요');
        $itemModifyForm['price'].parentElement.setValid(false);
        return;
    }
    if ($itemModifyForm['rate'].value === '') {
        dialog.showSimpleOk('상품수정 오류', '할인율을 입력해주세요');
        $itemModifyForm['rate'].parentElement.setValid(false);
        return;
    }
    if ($itemModifyForm['rate'].value < 0 || $itemModifyForm['rate'].value > 100) {
        dialog.showSimpleOk('상품수정 오류', '올바른 할인율을 입력해주세요');
        $itemModifyForm['rate'].parentElement.setValid(false);
        return;
    }
    if ($itemModifyForm['deliveryFee'].value === '') {
        dialog.showSimpleOk('상품수정 오류', '배송비를 입력해주세요');
        $itemModifyForm['deliveryFee'].parentElement.setValid(false);
        return;
    }
    if ($itemModifyForm['deliveryFee'].value < 0) {
        dialog.showSimpleOk('상품수정 오류', '올바른 배송비를 입력해주세요');
        $itemModifyForm['deliveryFee'].parentElement.setValid(false);
        return;
    }
    if ($itemModifyForm['deliveryCompany'].value === '') {
        dialog.showSimpleOk('상품수정 오류', '배송사를 입력해주세요');
        $itemModifyForm['deliveryCompany'].parentElement.setValid(false);
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('id', new URL(location.href).searchParams.get('productId'));
    formData.append('imageUrl', $itemModifyForm['productImage'].value);
    formData.append('name', $itemModifyForm['productName'].value);
    formData.append('brandId', $brand.value);
    formData.append('skinId', $skin.value);
    formData.append('size', $itemModifyForm['size'].value);
    formData.append('price', $itemModifyForm['price'].value);
    formData.append('deliveryFee', $itemModifyForm['deliveryFee'].value);
    formData.append('deliveryCompany', $itemModifyForm['deliveryCompany'].value);
    formData.append('deliveryAdd', $itemModifyForm['deliveryAdd'].value);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('상품수정', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        console.log(response.result)
        switch (response.result) {
            case 'failure':
                dialog.showSimpleOk('상품수정', '설마');
                break;
            case 'success':
                const xhr3 = new XMLHttpRequest();
                const formData3 = new FormData();
                formData3.append('productId', new URL(location.href).searchParams.get('productId'));
                xhr3.onreadystatechange = () => {
                    if (xhr3.readyState !== XMLHttpRequest.DONE) {
                        return;
                    }
                    if (xhr3.status < 200 || xhr3.status >= 300) {
                        dialog.showSimpleOk('성분추가', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
                        return;
                    }
                    const response3 = JSON.parse(xhr3.responseText);
                    switch (response3.result) {
                        case 'failure':
                            dialog.showSimpleOk('상품수정', '삭제실패');
                            break;
                        case 'success':
                            $ingredientContainer.querySelectorAll(':scope > .item').forEach((ingredient) => {
                                const xhr2 = new XMLHttpRequest();
                                const formData2 = new FormData();
                                formData2.append('korName', ingredient.querySelector(':scope > .korName').innerText);
                                formData2.append('score', ingredient.querySelector(':scope > .score').innerText);
                                formData2.append('productId', new URL(location.href).searchParams.get('productId'));
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
                                            dialog.showSimpleOk('상품수정', '성분추가실패');
                                            break;
                                        case 'success':
                                            break;
                                        default:
                                            dialog.showSimpleOk('상품수정', '알 수 없는 이유로 성분을 등록 하지 못하였습니다.');
                                    }
                                };
                                xhr2.open('POST', '/item/ingredient');
                                xhr2.send(formData2);
                            });
                            break;
                        default:
                            dialog.showSimpleOk('상품수정', '알 수 없는 이유로 성분을 등록 하지 못하였습니다.');
                    }
                };
                xhr3.open('DELETE', '/item/ingredient');
                xhr3.send(formData3);

                dialog.showSimpleOk('상품수정', '상품수정을 성공하였습니다.', {
                    onOkCallback: () => $itemModifyForm.querySelector(':scope > .button-container > a').click()
                });
                break;
            default:
                dialog.showSimpleOk('상품수정', '알 수 없는 이유로 상품수정을 하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('PATCH', '/item/');
    xhr.send(formData);
}

$ingredient.addEventListener('keyup', () => {
    if ($itemModifyForm['ingredient'].value === '') {
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
                    let cnt = 0;
                    if ($ingredientContainer.childElementCount !== 0) {
                        $ingredientContainer.querySelectorAll(':scope > .item').forEach(($ingredientChk) => {
                            if ($item.querySelector(':scope > .korName').innerText === $ingredientChk.querySelector(':scope > .korName').innerText) {
                                cnt++;
                            }
                        });
                    }
                    if (cnt > 0) {
                        dialog.showSimpleOk('성분추가', '이미 추가한 성분입니다.');
                        return;
                    }
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

ingredientRemoveButton();