const $rank = document.querySelector('#rank > .a-container > .button > .ranking');
const $skinContainer = document.querySelector('#rank > .b-container > .label-container > .label >.radio.skin');
const $ageContainer = document.querySelector('#rank > .b-container > .label-container > .label >.radio.age');
const $brandContainer = document.querySelector('#rank > .b-container > .label-container > .label >.radio.brand');
const $skin = document.querySelector('#rank > .skin-container');
const $age = document.querySelector('#rank > .age-container');
const $brand = document.querySelector('#rank > .brand-container');

$skinContainer.addEventListener('click', () => {
    $skin.classList.add('visible');
    $age.classList.remove('visible');
    $brand.classList.remove('visible');
});


$ageContainer.addEventListener('click', () => {
    $age.classList.add('visible');
    $skin.classList.remove('visible');
    $brand.classList.remove('visible');
});

$brandContainer.addEventListener('click', () => {
    $brand.classList.add('visible');
    $skin.classList.remove('visible');
    $age.classList.remove('visible');
});

$rank.addEventListener('click', () => {
    $skin.classList.add('visible');
    $age.classList.remove('visible');
});

