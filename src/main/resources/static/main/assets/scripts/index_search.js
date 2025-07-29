const $search = document.querySelectorAll('#main > .line > .item-box');

$search.forEach(($itemBox) => {
    $itemBox.querySelector(':scope > .---button-container').addEventListener('click', () => {

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        console.log($itemBox.querySelector(':scope > .---button-container > .productId').value)
        formData.append('productId', $itemBox.querySelector(':scope > .---button-container > .productId').value);
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