const $main = document.getElementById('main');

$main.querySelectorAll(':scope > .product-container > .item-box').forEach(($item) => {
    $item.querySelector(':scope > .--object-button.-color-red.delete').addEventListener('click', () => alert($item['cartDetailId']))
    $item.querySelector(':scope > .item-container > .price-container > .--object-button.-color-gray.plus').addEventListener('click', () => {
        alert(1);
    })
    $item.querySelector(':scope > .item-container > .price-container > .--object-button.-color-gray.minus').addEventListener('click', () => {
        alert(2);
    })
})