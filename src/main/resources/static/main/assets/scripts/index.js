const $knobController = document.querySelectorAll('#layout-content > .box > .controller > .knob');
const $checkBoxLabel = document.querySelector('#layout-content > .box > .controller > .checkbox-container');
const $startButton = $checkBoxLabel.querySelector(':scope > .icon.start');
const $pauseButton = $checkBoxLabel.querySelector(':scope > .icon.pause');
const $imageTable = document.getElementById('imageTable');
const $categoryButton = document.querySelector('#main > .A-container > .button');
let currentPage = -1;

console.log($categoryButton);

$categoryButton.addEventListener('click', () => {
    alert('!!');
})

$startButton.addEventListener('click', () => { // startButton 클릭 이벤트
    $pauseButton.classList.add('-selected');
    $startButton.classList.remove('-selected');
});

$pauseButton.addEventListener('click', () => { // pauseButton 클릭 이벤트
    $pauseButton.classList.remove('-selected');
    $startButton.classList.add('-selected');
});

$knobController.forEach(($knob) => {
    $knob.addEventListener('click', () => { // knob 클릭 일벤트
        $pauseButton.click();
        $knobController.forEach(($knob) => $knob.classList.remove('-selected'));
        $knob.classList.add('-selected');
    });
});

function autoFlip() {
    if (!$pauseButton.classList.contains('-selected')) { // pauseButton이 선택되어 있지 않으면 실행x
        return;
    }
    if (currentPage === -1) { // 초기값 -1이면 0으로 지정
        currentPage = 0;
    } else { // 증가
        currentPage++;
    }
    let product = 'product' + (currentPage % 5 + 1);
    let productImage = 'productImage' + (currentPage % 5 + 1);
    $knobController.forEach(($knob) => $knob.classList.remove('-selected'));
    document.getElementById(product).classList.add('-selected');
    $imageTable.scrollTo({top: 0, left: document.getElementById(productImage).offsetLeft}); // image 스크롤 위치선정
}

autoFlip();
setInterval(autoFlip, 3000); // 3초마다(실행 3000 -> 3초)

