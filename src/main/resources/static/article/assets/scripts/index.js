const $information = document.querySelector('#main > .page-container >.information-review > .two-container > .content.information');
const $review = document.querySelector('#main > .page-container >.information-review > .two-container > .content.review');
const $informationSelect = document.querySelector('#main > .page-container >.information-review > .information-page');
const $reviewSelect = document.querySelector('#main > .page-container >.information-review > .review-container');

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
