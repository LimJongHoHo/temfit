const $rank = document.querySelector('#rank > .a-container > .button > .ranking');
const $skinContainer = document.querySelector('#rank > .b-container > .label-container > .label >.radio.skin');
const $brandContainer = document.querySelector('#rank > .b-container > .label-container > .label >.radio.brand');
const $skin = document.querySelector('#rank > .skin-container');
const $brand = document.querySelector('#rank > .brand-container');

$skinContainer.addEventListener('click', () => {
    $skin.classList.add('visible');
    $brand.classList.remove('visible');
});

$brandContainer.addEventListener('click', () => {
    $brand.classList.add('visible');
    $skin.classList.remove('visible');
});

$rank.addEventListener('click', () => {
    location.reload();
});

$skinContainer.click();

const productItems = () => {
    for (const product of targetProducts) {
        $skinContainer.insertAdjacentHTML('beforeend', `
                <div class="line">
                    <div class="item-box">
                        <img class="medal" src="/assets/images/1st-medal.png" alt="1st-medal">
                        <a class="title">${product['name']}</a>
                        <a class="caption">${product['name']}</a>
                    </div>
                </div>`);
    }
};

$skin.querySelectorAll(':scope > .label').forEach(($skinItem) => {
    $skinItem.querySelector(':scope > .caption').addEventListener('click', () => {
        alert('!')

    })
});