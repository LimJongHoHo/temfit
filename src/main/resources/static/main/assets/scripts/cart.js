const $main = document.getElementById('main');
const $itemList = $main.querySelectorAll(':scope > .product-container > .item-box');
$itemList.forEach(($item) => {
    $item.querySelector(':scope > .--object-button.-color-red.delete').addEventListener('click', () => {
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('cartDetailId', $item.querySelector(':scope > .cartDetailId').value);
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {
                dialog.showSimpleOk('장바구니 삭제', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
                return;
            }
            const response = JSON.parse(xhr.responseText);
            switch (response.result) {
                case 'failure':
                    dialog.showSimpleOk('장바구니 삭제', '세션이 만료되었거나 게시글을 작성할 권한이 없습니다. 관리자에게 문의해 주세요.');
                    break;
                case 'success':
                    $item.remove();
                    if ($itemList.length === 1) {
                        location.reload();
                    }
                    break;
                default:
                    dialog.showSimpleOk('장바구니 삭제', '알 수 없는 이유로 장바구니를 삭제하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        };
        xhr.open('DELETE', '/item/cart-detail');
        xhr.send(formData);
    });
    $item.querySelector(':scope > .item-container > .price-container > .--object-button.-color-gray.minus').addEventListener('click', () => {
        const quantity = $item.querySelector(':scope > .item-container > .price-container > .--object-button.count');
        if (quantity.innerText === '1') {
            dialog.showSimpleOk('상품 제거', '1보다 작을 수 없습니다.');
            return;
        }
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('cartDetailId', $item.querySelector(':scope > input').value);
        formData.append('calc', 'minus');
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {
                dialog.showSimpleOk('상품 제거', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
                return;
            }
            const response = JSON.parse(xhr.responseText);
            switch (response.result) {
                case 'failure':
                    dialog.showSimpleOk('상품 제거', '세션이 만료되었거나 게시글을 작성할 권한이 없습니다. 관리자에게 문의해 주세요.');
                    break;
                case 'success':
                    quantity.innerText = parseInt(quantity.innerText) - 1;
                    $item.querySelector(':scope > .totalPrice').value = parseInt($item.querySelector(':scope > .totalPrice').value) - parseInt($item.querySelector(':scope > .price').value);
                    $item.querySelector(':scope > .total-price').innerText = new Intl.NumberFormat('ko-KR').format($item.querySelector(':scope > .totalPrice').value) + '원';
                    break;
                default:
                    dialog.showSimpleOk('상품 제거', '알 수 없는 이유로 장바구니를 삭제하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        };
        xhr.open('PATCH', '/item/cart-detail');
        xhr.send(formData);
    })
    $item.querySelector(':scope > .item-container > .price-container > .--object-button.-color-gray.plus').addEventListener('click', () => {
        const quantity = $item.querySelector(':scope > .item-container > .price-container > .--object-button.count');

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('cartDetailId', $item.querySelector(':scope > input').value);
        formData.append('calc', 'plus');
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {
                dialog.showSimpleOk('상품 추가', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
                return;
            }
            const response = JSON.parse(xhr.responseText);
            switch (response.result) {
                case 'failure':
                    dialog.showSimpleOk('상품 추가', '세션이 만료되었거나 게시글을 작성할 권한이 없습니다. 관리자에게 문의해 주세요.');
                    break;
                case 'success':
                    quantity.innerText = parseInt(quantity.innerText) + 1;
                    $item.querySelector(':scope > .totalPrice').value = parseInt($item.querySelector(':scope > .totalPrice').value) + parseInt($item.querySelector(':scope > .price').value);
                    $item.querySelector(':scope > .total-price').innerText = new Intl.NumberFormat('ko-KR').format($item.querySelector(':scope > .totalPrice').value) + '원';
                    break;
                default:
                    dialog.showSimpleOk('상품 추가', '알 수 없는 이유로 장바구니를 삭제하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        };
        xhr.open('PATCH', '/item/cart-detail');
        xhr.send(formData);
    })
});