const $itemWriteForm = document.getElementById('itemWriteForm');
const $brand = document.getElementById('brand');
const $skin = document.getElementById('skin');
const $labels = $itemWriteForm.querySelectorAll(':scope > .--object-label-row')
const $image = $itemWriteForm.querySelector(':scope > .--object-label-row > .image');

$itemWriteForm['productImage'].addEventListener('focusout', () => {
    $itemWriteForm['productImage'].parentElement.setValid(true)
    if ($itemWriteForm['productImage'].value === '') {
        $itemWriteForm['productImage'].parentElement.setValid(false, '커버 이미지 주소를 입력해 주세요.');
        return;
    }
    if (!$itemWriteForm['productImage'].value.startsWith('http://') && !$itemWriteForm['productImage'].value.startsWith('https://')) {
        $itemWriteForm['productImage'].parentElement.setValid(false, '이미지 주소는 "http://" 혹은 "https://"로 시작하여야 합니다.');
        return;
    }
    $image.setAttribute('src', $itemWriteForm['productImage'].value);
    $itemWriteForm['productImage'].parentElement.setValid(true);
});

$itemWriteForm.onsubmit = (e) => {
    e.preventDefault();

    $labels.forEach(($label) => $label.setValid(true));
    if ($itemWriteForm['productImage'].value === '') {
        dialog.showSimpleOk('상품등록 오류', '커버 이미지 주소를 입력해주세요.');
        $itemWriteForm['productImage'].parentElement.setValid(false, '커버 이미지 주소를 입력해 주세요.');
        return;
    }
    if (!$itemWriteForm['productImage'].value.startsWith('http://') && !$itemWriteForm['productImage'].value.startsWith('https://')) {
        dialog.showSimpleOk('상품등록 오류', '이미지 주소는 "http://" 혹은 "https://"로 시작하여야 합니다.');
        $itemWriteForm['productImage'].parentElement.setValid(false, '이미지 주소는 "http://" 혹은 "https://"로 시작하여야 합니다.');
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
        dialog.showSimpleOk('상품등록 오류', '피부유형을 선택해주세요');
        $skin.parentElement.setValid(false);
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
    formData.append('imageUrl', $itemWriteForm['productImage'].value);
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