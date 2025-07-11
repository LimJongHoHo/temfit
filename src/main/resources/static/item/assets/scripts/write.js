const $itemWriteForm = document.getElementById('itemWriteForm');
const $label = $itemWriteForm.querySelector(':scope > .--object-label-row');
const $image = $label.querySelector(':scope > .image');

$itemWriteForm['productImage'].addEventListener('focusout', () => {
    $image.setAttribute('src', '/assets/images/view.png')
    $label.setValid(true)
    if ($itemWriteForm['productImage'].value === '') {
        $label.setValid(false, '커버 이미지 주소를 입력해 주세요.');
        return;
    }
    if (!$itemWriteForm['productImage'].value.startsWith('http://') && !$itemWriteForm['productImage'].value.startsWith('https://')) {
        $itemWriteForm['productImage'].setValid(false, '올바른 이미지 주소를 입력해 주세요. 이미지 주소는 "http://" 혹은 "https://"로 시작하여야 합니다.');
        return;
    }
    $image.setAttribute('src', $itemWriteForm['productImage'].value);
    $label.setValid(true);
});

$itemWriteForm.onsubmit = (e) => {
    e.preventDefault();
}