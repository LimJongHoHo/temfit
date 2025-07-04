const $information = document.querySelector('#main > .page-container >.information-review > .two-container > .content.information');
const $review = document.querySelector('#main > .page-container >.information-review > .two-container > .content.review');
const $informationSelect = document.querySelector('#main > .page-container >.information-review > .information-page');
const $reviewSelect = document.querySelector('#main > .page-container >.information-review > .review-container');
const $reviewAllSelect = document.querySelector('#main > .page-container >.information-review > .review-container > .button-container');


$review.addEventListener('click', () =>{
    $review.setSelected(true);
    $information.setSelected(false);
    $reviewSelect.setSelected(true);
    $informationSelect.setSelected(false);

});

$information.addEventListener('click', () =>{
    $review.setSelected(false);
    $information.setSelected(true);
    $reviewSelect.setSelected(false);
    $informationSelect.setSelected(true);
});

$reviewAllSelect.addEventListener('click', () =>{
    const id = new URL(location.href).searchParams.get('id');
    location.href = `/review/list?id=${id}`;
});




