const $knobController = document.querySelectorAll('#layout-content > .box > .controller > .knob');
const $checkBoxLabel = document.querySelector('#layout-content > .box > .controller > .checkbox-container');
const $startButton = $checkBoxLabel.querySelector(':scope > .icon.start');
const $pauseButton = $checkBoxLabel.querySelector(':scope > .icon.pause');
const $imageTable = document.getElementById('imageTable');
const $categoryButton = document.querySelector('#main > .A-container > .button');
const $skin = document.querySelector('#main > .A-container > .item-container > .skin-container');
const $skinLabels = document.querySelectorAll('#main > .A-container > .item-container > .skin-label > .label');


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
            $skin.innerHTML = '';
            let skinHtml = ``;

            for (const product of products) {
                let medal = '';
                if (product.num === 1) {
                    medal = `
                        <div class="item-box">
                            <img class="medal" src="/assets/images/1st-medal.png" alt="1medal"/>`;
                } else if(product.num === 2) {
                    medal = `
                        <div class="item-box">
                            <img class="medal" src="/assets/images/2nd-medal.png" alt="2medal"/>`;
                } else if(product.num === 3) {
                    medal = `
                        <div class="item-box">
                            <img class="medal" src="/assets/images/3rd-medal.png" alt="3medal"/>`;
                } else {
                    medal = `<span>${product.num}</span>`;
                }
                skinHtml += `
                        <img class="item" alt=""/> 
                        <div class="container">
                            <div class="brand-container">
                                <a class="title">${product.name}</a>
                                <a class="caption">${product.brandName}</a>
                            </div>
                            <div class="star-container">
                                <div class="star-scope">
                                    <span class="score">${product.discountRate}</span>
                                    <span class="number">${product.price}</span>
                                </div>
                            </div>
                        </div>
                    </div>`;
                skinHtml = medal + skinHtml;
            }

            $skin.innerHTML = skinHtml;

        };
        xhr.open('POST', '/skinId');
        xhr.send(formData)

    })
});


let currentPage = -1;

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
    let imageCount = document.querySelectorAll('#layout-content > .box > .page-container > .page').length;
    let product = 'product' + (currentPage % imageCount + 1);
    let productImage = 'productImage' + (currentPage % imageCount + 1);
    $knobController.forEach(($knob) => $knob.classList.remove('-selected'));
    document.getElementById(product).classList.add('-selected');
    $imageTable.scrollTo({top: 0, left: document.getElementById(productImage).offsetLeft}); // image 스크롤 위치선정
}

autoFlip();
setInterval(autoFlip, 3000); // 3초마다(실행 3000 -> 3초)