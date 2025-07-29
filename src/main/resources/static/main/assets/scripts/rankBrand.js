const $rank = document.querySelector('#rank > .a-container > .button > .ranking');
const $skinContainer = document.querySelector('#rank > .b-container > .label-container > .label >.radio.skin');
const $brandContainer = document.querySelector('#rank > .b-container > .label-container > .label >.radio.brand');
const $productContainer = document.querySelector('#rank > .b-container > .label-container > .label >.radio.product');
const $skin = document.querySelector('#rank > .skin-container');
const $brand = document.querySelector('#rank > .brand-container');
const $product = document.querySelector('#rank > .product-container');
const $brandLabels = document.querySelectorAll('#rank > .brand-container > .brand-label > .label');
const $skinLabels = document.querySelectorAll('#rank > .skin-container > .skin-label > .label');
const $productLabels = document.querySelectorAll('#rank > .product-container > .line > .item-box');
const $skinBox = document.querySelector('#rank > .skin-container > .skin-box');
const $skinLabel = document.querySelector('#rank > .skin-container > .skin-label > .label:first-child');

$productContainer.addEventListener('click', () => {
    $product.classList.add('visible');
    $skin.classList.remove('visible');
    $brand.classList.remove('visible');
})

$skinContainer.addEventListener('click', () => {
    $skin.classList.add('visible');
    $brand.classList.remove('visible');
    $product.classList.remove('visible');
    // $skin.querySelector(':scope > .skin-label > .label:first-of-type > input').checked(true);
});

$brandContainer.addEventListener('click', () => {
    $brand.classList.add('visible');
    $skin.classList.remove('visible');
    $product.classList.remove('visible');
});

$rank.addEventListener('click', () => {
    location.reload();
});

$brandContainer.click();

$productLabels.forEach(($lines) => {
    $lines.querySelector(':scope > .---button-container').addEventListener('click', () => {

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        console.log($lines.querySelector(':scope > .---button-container > .productId').value)
        formData.append('productId', $lines.querySelector(':scope > .---button-container > .productId').value);
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {
                alert('요청중 오류가 발생하였습니다. 잠시 후 다시 시대해 주세요.');
                return;
            }

            const response = JSON.parse(xhr.responseText);

            location.href = `/article/?id=${response.articleId}`

        };
        xhr.open('POST', '/productId');
        xhr.send(formData);
    });
});

$brandLabels.forEach(($label) => {
    $label.addEventListener('click', () => {

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('brandId', $label.querySelector(':scope > .brandId').value);

        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {
                alert('요청을 처리하는 도 중 오류가 발생하였습니다.');
                return;
            }
            const response = JSON.parse(xhr.responseText);

            const products = response.products;
            $brandLabels.forEach(($brand) => $brand.querySelector(':scope > .brand-box').innerHTML = '');
            let brandHtml = ``;

            for (const product of products) {
                brandHtml += `
                <div class="line">
                     <input hidden type="hidden" class="productId" value="${product.id}">
                     <div class="item-box">
                        <img class="item" src="${product.imageUrl}" alt="${product.name}">
                        <a class="title">${product.brandName}</a>
                        <a class="caption">${product.name}</a>
                    </div>
                </div> `;
                brandHtml;
            }
            $label.querySelector(':scope > .brand-box').innerHTML = brandHtml;


            $label.querySelectorAll(':scope > .brand-box > .line').forEach(($line) => {
                $line.addEventListener('click', () => {
                    const xhr = new XMLHttpRequest();
                    const formData = new FormData();
                    formData.append('productId', $line.querySelector(':scope > .productId').value);
                    xhr.onreadystatechange = () => {
                        if (xhr.readyState !== XMLHttpRequest.DONE) {
                            return;
                        }
                        if (xhr.status < 200 || xhr.status >= 300) {

                            return;
                        }
                        const response = JSON.parse(xhr.responseText);

                        location.href = `/article/?id=${response.articleId}`
                    };
                    xhr.open('POST', '/productId');
                    xhr.send(formData);
                })
            })
        };
        xhr.open('POST', '/brandId');
        xhr.send(formData)
    })
});

$skinLabels.forEach(($label) => {
    $label.addEventListener('click', () => {

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('skinId', $label.querySelector(':scope > .skinId').value);

        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {
                alert('요청을 처리하는 도 중 오류가 발생하였습니다.');
                return;
            }
            const response = JSON.parse(xhr.responseText);

            const products = response.products;
            $skinBox.innerHTML = '';
            let skinHtml = ``;

            for (const product of products) {
                let medal = '';
                if (product.num === 1) {
                    medal = `
                        <button class="item-box" type="button">
                            <img class="medal" src="/assets/images/1st-medal.png" alt="1medal"/>`;
                } else if(product.num === 2) {
                    medal = `
                        <button class="item-box" type="button">
                            <img class="medal" src="/assets/images/2nd-medal.png" alt="2medal"/>`;
                } else if(product.num === 3) {
                    medal = `
                        <button class="item-box" type="button">
                            <img class="medal" src="/assets/images/3rd-medal.png" alt="3medal"/>`;
                } else {
                    medal = `<span>${product.num}</span>`;
                }
                skinHtml += `
                        <img class="item" src="${product.imageUrl}" alt="${product.name}">
                        <div class="container">
                            <input hidden type="hidden" class="skinProductId" value="${product.id}">
                            <div class="brand-container">
                                <a class="title">${product.brandName}</a>
                                <a class="caption">${product.name}</a>
                            </div>
                            <div class="star-container">
                                <div class="star-scope">
                                    <span class="score">${product.discountRate}%</span>
                                    <span class="number">${product.price}원</span>
                                </div>
                            </div>
                        </div>
                    </button> `;
                skinHtml = medal + skinHtml;
            }
            $skinBox.innerHTML = skinHtml;

            $label.parentElement.parentElement.querySelector(':scope > .skin-box').innerHTML = skinHtml;

            $label.parentElement.parentElement.querySelectorAll(':scope > .skin-box > .item-box').forEach(($itemBox) => {
                $itemBox.addEventListener('click', () => {
                    const xhr = new XMLHttpRequest();
                    const formData = new FormData();
                    console.log($itemBox.querySelector(':scope > .container  > .skinProductId').value)
                    formData.append('productId', $itemBox.querySelector(':scope > .container  > .skinProductId').value);
                    xhr.onreadystatechange = () => {
                        if (xhr.readyState !== XMLHttpRequest.DONE) {
                            return;
                        }
                        if (xhr.status < 200 || xhr.status >= 300) {

                            return;
                        }
                        const response = JSON.parse(xhr.responseText);

                        location.href = `/article/?id=${response.articleId}`
                    };
                    xhr.open('POST', '/productId');
                    xhr.send(formData);
                })
            })
        };
        xhr.open('POST', '/skinId');
        xhr.send(formData)
    })
});

$skinLabel.click();

