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