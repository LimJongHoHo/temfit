const $pay = document.getElementById('payForm');
const $termContainer = $pay.querySelector(':scope > .container.term');
const nameRegex = new RegExp('^([가-힣]{2,5})$')
const contactSecondRegex = new RegExp('^(\\d{3,4})$');
const contactThirdRegex = new RegExp('^(\\d{4})$');

$pay['addressFindButton'].addEventListener('click', () => {
    const $addressFindDialog = document.getElementById('addressFindDialog');
    const $modal = $addressFindDialog.querySelector(':scope > .modal');
    $addressFindDialog.onclick = (e) => {
        $addressFindDialog.hide();
    };
    new daum.Postcode({
        width: '100%',
        height: '100%',
        oncomplete: (data) => {
            $addressFindDialog.hide();
            $pay['addressPostal'].value = data['zonecode'];
            $pay['addressPrimary'].value = data['roadAddress'];
            $pay['addressSecondary'].focus();
            $pay['addressSecondary'].select();
        }
    }).embed($modal);
    $addressFindDialog.show();
});

$pay.onsubmit = (e) => {
    e.preventDefault();

    const $nameLabel = $pay.querySelector('.--object-label:has(input[name="name"])');
    const $contactLabel = $pay.querySelector('.--object-label:has(input[name="contactSecond"])');
    const $addressLabel = $pay.querySelector('.--object-label:has(input[name="addressPostal"])');
    const $labels = [$addressLabel, $nameLabel, $contactLabel]
    $labels.forEach(($label) => $label.setValid(true));

    if ($pay['addressPostal'].value === '') {
        dialog.showSimpleOk('결제', '배송지를 입력해 주세요.');
        $addressLabel.setValid(false, '주소를 입력해 주세요.');
        $pay['addressPostal'].focus();
        return;
    }

    if ($pay['name'].value === '') {
        dialog.showSimpleOk('결제', '이름을 입력해 주세요.');
        $nameLabel.setValid(false, '이름을 입력해 주세요.');
        $pay['name'].focus();
        return;
    }
    if (!nameRegex.test($pay['name'].value)) {
        $nameLabel.setValid(false, '올바른 이름을 입력해 주세요.');
        $pay['name'].focus();
        return;
    }
    if ($pay['contactMvno'].value === '-1') {
        dialog.showSimpleOk('결제', '통신사를 선택해 주세요.');
        $contactLabel.setValid(false, '통신사를 선택해 주세요.');
        $pay['contactMvno'].focus();
        return;
    }
    if ($pay['contactSecond'].value === '') {
        dialog.showSimpleOk('결제', '연락처를 입력해 주세요.');
        $contactLabel.setValid(false, '연락처를 입력해 주세요.');
        $pay['contactSecond'].focus();
        return;
    }
    if (!contactSecondRegex.test($pay['contactSecond'].value)) {
        $contactLabel.setValid(false, '올바른 연락처를 입력해 주세요');
        $pay['contactSecond'].focus();
        return;
    }
    if ($pay['contactThird'].value === '') {
        dialog.showSimpleOk('결제', '연락처를 입력해 주세요.');
        $contactLabel.setValid(false, '연락처를 입력해 주세요.');
        $pay['contactThird'].focus();
        return;
    }
    if (!contactThirdRegex.test($pay['contactThird'].value)) {
        $contactLabel.setValid(false, '올바른 연락처를 입력해 주세요');
        $pay['contactThird'].focus();
        return;
    }

    if (!$pay['agreeServiceTerm'].checked) {
        dialog.showSimpleOk('결제', '주문내용 확인 및 결제에 동의해 주세요.');
        return;
    }
    if (!$pay['agreePrivacyTerm'].checked) {
        dialog.showSimpleOk('결제', '개인정보 이용약관에 동의해 주세요.');
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('deliveryContent', $pay['content'].value);
    formData.append('name', $pay['name'].value);
    formData.append('contactMvnoCode', $pay['contactMvno'].value);
    formData.append('contactFirst', $pay['contactFirst'].value);
    formData.append('contactSecond', $pay['contactSecond'].value);
    formData.append('contactThird', $pay['contactThird'].value);
    formData.append('addressPostal', $pay['addressPostal'].value);
    formData.append('addressPrimary', $pay['addressPrimary'].value);
    formData.append('addressSecondary', $pay['addressSecondary'].value);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('결제', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure_session_expired':
                dialog.showSimpleOk('결제', '세션이 만료되었습니다. 관리자에게 문의해 주세요.');
                break;
            case 'failure':
                dialog.showSimpleOk('결제', '결제에 실패하였습니다.');
                break;
            case 'success':
                dialog.showSimpleOk('결제', '결제를 성공하였습니다.');
                location.href = `/item/pay-complete?paymentId=${response.id}`;
                break;
            default:
                dialog.showSimpleOk('결제', '알 수 없는 이유로 결제하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('POST', '/item/');
    xhr.send(formData);
};
