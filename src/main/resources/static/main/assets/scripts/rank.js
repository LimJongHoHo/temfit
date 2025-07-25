const $rank = document.querySelector('#rank > .a-container > .button > .ranking');
const $skinContainer = document.querySelector('#rank > .b-container > .label-container > .label >.radio.skin');
const $brandContainer = document.querySelector('#rank > .b-container > .label-container > .label >.radio.brand');
const $skin = document.querySelector('#rank > .skin-container');
const $brand = document.querySelector('#rank > .brand-container');
const $brandLabels = document.querySelectorAll('#rank > .brand-container > .brand-label > .brand-con');
const $brandBox = document.querySelector('#rank > .brand-container > .brand-label > .brand-box');

$skinContainer.addEventListener('click', () => {
    $skin.classList.add('visible');
    $brand.classList.remove('visible');
    // $skin.querySelector(':scope > .skin-label > .label:first-of-type > input').checked(true);
});

$brandContainer.addEventListener('click', () => {
    $brand.classList.add('visible');
    $skin.classList.remove('visible');
});

$rank.addEventListener('click', () => {
    location.reload();
});

$skinContainer.click();

$skin.querySelectorAll(':scope > .skin-label > .label').forEach(($skinItem) => {
    $skinItem.querySelector(':scope > .caption').addEventListener('click', () => {

    })
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
            $brandBox.innerHTML = '';
            let brandHtml = ``;

            for (const product of products) {
                brandHtml += `
                    <span>dkdk</span> `;
                brandHtml += brandHtml;
            }

            $brandBox.innerHTML = brandHtml;

        };
        xhr.open('POST', '/brandId');
        xhr.send(formData)
    })
});


