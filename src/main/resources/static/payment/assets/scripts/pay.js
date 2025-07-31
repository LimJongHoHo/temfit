const $paymentForm = document.getElementById('payForm');
const nameRegex = new RegExp('^([가-힣]{2,5})$')
const contactSecondRegex = new RegExp('^(\\d{3,4})$');
const contactThirdRegex = new RegExp('^(\\d{4})$');
const totalPrice = document.getElementById('totalPrice');

const $payForm = document.getElementById('payForm');
const $priceContainer = $payForm.querySelector(':scope > .product-container > .item-box > .price-container');
const $totalPrice = $payForm.querySelector(':scope > .price-container');

$priceContainer.querySelector(':scope > .--object-button.minus').addEventListener('click', () => {
    const count = $priceContainer.querySelector(':scope > .--object-button.count');
    if (count.innerText === '1') {
        dialog.showSimpleOk('상품 제거', '1보다 작을 수 없습니다.');
        return;
    }
    count.innerText = parseInt(count.innerText) - 1;
    const quantity = parseInt($priceContainer.querySelector(':scope > .--object-button.count').innerText);
    $totalPrice.querySelector(':scope > .container > .product-price').innerText = new Intl.NumberFormat('ko-KR').format($totalPrice.querySelector(':scope > .container > .price').value * quantity) + '원';
    $totalPrice.querySelector(':scope > .total-pay > .pay-price').innerText = new Intl.NumberFormat('ko-KR').format($totalPrice.querySelector(':scope > .container > .price').value * quantity) + '원';
});

$priceContainer.querySelector(':scope > .--object-button.plus').addEventListener('click', () => {
    const count = $priceContainer.querySelector(':scope > .--object-button.count');

    count.innerText = parseInt(count.innerText) + 1;
    const quantity = parseInt($priceContainer.querySelector(':scope > .--object-button.count').innerText);
    $totalPrice.querySelector(':scope > .container > .product-price').innerText = new Intl.NumberFormat('ko-KR').format($totalPrice.querySelector(':scope > .container > .price').value * quantity) + '원';
    $totalPrice.querySelector(':scope > .total-pay > .pay-price').innerText = new Intl.NumberFormat('ko-KR').format($totalPrice.querySelector(':scope > .container > .price').value * quantity) + '원';

});


$paymentForm['addressFindButton'].addEventListener('click', () => {
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
            $paymentForm['addressPostal'].value = data['zonecode'];
            $paymentForm['addressPrimary'].value = data['roadAddress'];
            $paymentForm['addressSecondary'].focus();
            $paymentForm['addressSecondary'].select();
        }
    }).embed($modal);
    $addressFindDialog.show();
});

$paymentForm.onsubmit = (e) => {
    e.preventDefault();

    const $nameLabel = $paymentForm.querySelector('.--object-label:has(input[name="name"])');
    const $contactLabel = $paymentForm.querySelector('.--object-label:has(input[name="contactSecond"])');
    const $addressLabel = $paymentForm.querySelector('.--object-label:has(input[name="addressPostal"])');
    const $labels = [$addressLabel, $nameLabel, $contactLabel]
    $labels.forEach(($label) => $label.setValid(true));

    if ($paymentForm['addressPostal'].value === '') {
        dialog.showSimpleOk('결제', '배송지를 입력해 주세요.');
        $addressLabel.setValid(false, '주소를 입력해 주세요.');
        $paymentForm['addressPostal'].focus();
        return;
    }

    if ($paymentForm['name'].value === '') {
        dialog.showSimpleOk('결제', '이름을 입력해 주세요.');
        $nameLabel.setValid(false, '이름을 입력해 주세요.');
        $paymentForm['name'].focus();
        return;
    }
    if (!nameRegex.test($paymentForm['name'].value)) {
        $nameLabel.setValid(false, '올바른 이름을 입력해 주세요.');
        $paymentForm['name'].focus();
        return;
    }
    if ($paymentForm['contactMvno'].value === '-1') {
        dialog.showSimpleOk('결제', '통신사를 선택해 주세요.');
        $contactLabel.setValid(false, '통신사를 선택해 주세요.');
        $paymentForm['contactMvno'].focus();
        return;
    }
    if ($paymentForm['contactSecond'].value === '') {
        dialog.showSimpleOk('결제', '연락처를 입력해 주세요.');
        $contactLabel.setValid(false, '연락처를 입력해 주세요.');
        $paymentForm['contactSecond'].focus();
        return;
    }
    if (!contactSecondRegex.test($paymentForm['contactSecond'].value)) {
        $contactLabel.setValid(false, '올바른 연락처를 입력해 주세요');
        $paymentForm['contactSecond'].focus();
        return;
    }
    if ($paymentForm['contactThird'].value === '') {
        dialog.showSimpleOk('결제', '연락처를 입력해 주세요.');
        $contactLabel.setValid(false, '연락처를 입력해 주세요.');
        $paymentForm['contactThird'].focus();
        return;
    }
    if (!contactThirdRegex.test($paymentForm['contactThird'].value)) {
        $contactLabel.setValid(false, '올바른 연락처를 입력해 주세요');
        $paymentForm['contactThird'].focus();
        return;
    }
    if (!$paymentForm['agreeServiceTerm'].checked) {
        dialog.showSimpleOk('결제', '주문내용 확인 및 결제에 동의해 주세요.');
        return;
    }
    if (!$paymentForm['agreePrivacyTerm'].checked) {
        dialog.showSimpleOk('결제', '개인정보 이용약관에 동의해 주세요.');
        return;
    }
    $pay.querySelector(':scope > .total').innerText = `${totalPrice.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",")}원`
    $payCover.classList.add('visible');
    $pay.classList.add('visible');
};

const $payCover = document.getElementById('payCover');
const $pay = document.getElementById('pay');

const hidePay = () => {
    $payCover.classList.remove('visible');
    $pay.classList.remove('visible');
}

$pay.querySelector(':scope > .button-container > .button.cancel').addEventListener('click', hidePay);

$pay.querySelector(':scope > .button-container > .button.confirm').addEventListener('click', () => {

    const date = new Date();
    const imp = window.IMP;

    imp.init('imp54886024');
    imp.request_pay({
        pg: 'kakaopay.TC0ONETIME',
        pay_method: 'card',
        merchant_uid: `IMP-${date.getTime()}`,
        name: 'Temfit',
        amount: totalPrice,
        buyer_email: 'vkdlxj321@naver.com',
        buyer_name: '임종호'
    }, (resp) => {
        if (resp.success === true) {
            const xhr = new XMLHttpRequest();
            const formData = new FormData();
            formData.append('deliveryContent', $paymentForm['content'].value);
            formData.append('name', $paymentForm['name'].value);
            formData.append('contactMvnoCode', $paymentForm['contactMvno'].value);
            formData.append('contactFirst', $paymentForm['contactFirst'].value);
            formData.append('contactSecond', $paymentForm['contactSecond'].value);
            formData.append('contactThird', $paymentForm['contactThird'].value);
            formData.append('addressPostal', $paymentForm['addressPostal'].value);
            formData.append('addressPrimary', $paymentForm['addressPrimary'].value);
            formData.append('addressSecondary', $paymentForm['addressSecondary'].value);
            formData.append('totalPrice', totalPrice);
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
                        dialog.showSimpleOk('결제','결제가 완료되었습니다.', {
                                onOkCallback: () => location.href = `/payment/pay-complete?id=${response.id}`
                            });
                        break;
                    default:
                        dialog.showSimpleOk('결제', '알 수 없는 이유로 결제하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            };
            xhr.open('POST', '/payment/');
            xhr.send(formData);
        } else {
            dialog.showSimpleOk(`결제에 실패하였습니다. (${resp['error_msg']})`);
        }
    });
});

