const emailRegex = new RegExp('^(?=.{8,50}$)([\\da-z\\-_.]{4,})@([\\da-z][\\da-z\\-]*[\\da-z]\\.)?([\\da-z][\\da-z\\-]*[\\da-z])\\.([a-z]{2,15})(\\.[a-z]{2,3})?$');
const emailCodeRegex = new RegExp('^(\\d{6})$');
const passwordRegex = new RegExp('^([\\da-zA-Z`~!@#$%^&*()\-_=+\\[{\\]}\\\\|;:\'",<.>/?]{8,50})$');
const nameRegex = new RegExp('^([가-힣]{2,5})$')
const contactSecondRegex = new RegExp('^(\\d{3,4})$');
const contactThirdRegex = new RegExp('^(\\d{4})$');
const $loading = document.getElementById('loading');
const $recoverForm = document.getElementById('recoverForm');

$recoverForm['pEmailCodeSendButton'].addEventListener('click', () => {
    const $emailLabel = $recoverForm.querySelector('label:has(input[name="pEmail"])');

    if ($recoverForm['pEmail'].value === '') {
        $emailLabel.setValid(false, '이메일을 입력해 주세요.');
        $recoverForm['pEmail'].focus();
        return;
    }
    if (!emailRegex.test($recoverForm['pEmail'].value)) {
        $emailLabel.setValid(false, '올바른 이메일을 입력해 주세요.');
        $recoverForm['pEmail'].focus();
        $recoverForm['pEmail'].select();
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', $recoverForm['pEmail'].value);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        $loading.hide();
        if (xhr.status < 200 || xhr.status >= 300) {

            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure_absent':
                dialog.showSimpleOk('인증번호 전송', `입력하신 이메일 '${$recoverForm['pEmail'].value}'을/를 사용 중인 계정을 찾지 못하였습니다.`);
                break;
            case 'success':
                $recoverForm['pEmailSalt'].value = response.salt;
                $recoverForm['pEmail'].setDisabled(true);
                $recoverForm['pEmailCodeSendButton'].setDisabled(true);
                $recoverForm['pEmailCode'].setDisabled(false);
                $recoverForm['pEmailCodeVerifyButton'].setDisabled(false);
                $recoverForm['pEmailCode'].focus();
                dialog.showSimpleOk('인증번호 전송', '입력하신 이메일로 인증번호를 전송하였습니다. 해당 인증번호는 10분간만 유효하니 유의해 주세요.');
                break;
            default:
                dialog.showSimpleOk('인증번호 전송','알 수 없는 이유로 인증번호를 전송하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('POST', '/user/recover-password-email');
    xhr.send(formData);
    $loading.show();
});

$recoverForm['pEmailCodeVerifyButton'].addEventListener('click', () => {
    const $emailLabel = $recoverForm.querySelector('label:has(input[name="pEmailCode"])');
    
    if ($recoverForm['pEmailCode'].value === '') {
        $emailLabel.setValid(false, '인증번호를 입력해 주세요.');
        $recoverForm['pEmailCode'].focus();
        return;
    }
    if (!emailCodeRegex.test($recoverForm['pEmailCode'].value)) {
        $emailLabel.setValid(false, '올바른 인증번호를 입력해 주세요.');
        $recoverForm['pEmailCode'].focus();
        $recoverForm['pEmailCode'].select();
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();

    formData.append('email', $recoverForm['pEmail'].value);
    formData.append('code', $recoverForm['pEmailCode'].value);
    formData.append('salt', $recoverForm['pEmailSalt'].value);

    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('인증번호 확인', '요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure_expired':
                $recoverForm['pEmailSalt'].value = 'response.salt';
                $recoverForm['pEmail'].setDisabled(false);
                $recoverForm['pEmailCodeSendButton'].setDisabled(false);
                $recoverForm['pEmailCode'].value = '';
                $recoverForm['pEmailCode'].setDisabled(true);
                $recoverForm['pEmailCodeVerifyButton'].setDisabled(true);
                $recoverForm['pEmailCode'].focus();
                dialog.showSimpleOk('인증번호 확인', '인증 정보가 만료되었습니다. 이메일 인증을 다시 진행해 주세요.');
                break;
            case 'success':
                $recoverForm['pEmailCode'].setDisabled(true);
                $recoverForm['pEmailCodeVerifyButton'].setDisabled(true);
                $recoverForm['pPassword'].setDisabled(false).focus();
                $recoverForm['pPasswordCheck'].setDisabled(false).focus();
                dialog.showSimpleOk('인증번호 확인', '이메일이 정상적으로 인증되었습니다.');
                break;
            default:
                dialog.showSimpleOk('인증번호 확인', '인증번호가 올바르지 않습니다. 다시 확인해 주세요.', () => {
                    $recoverForm['pEmailCode'].focus();
                    $recoverForm['pEmailCode'].select();
                });
        }
    };
    xhr.open('PATCH', '/user/recover-password-email');
    xhr.send(formData);
});

$recoverForm.onsubmit = (e) => {
    e.preventDefault();

    if ($recoverForm['type'].value === 'email') {
        const $nameLabel = $recoverForm.querySelector('label:has(input[name="eName"])')
        const $contactLabel = $recoverForm.querySelector('label:has(input[name="eContactSecond"])')

        $nameLabel.setValid(true);
        $contactLabel.setValid(true);

        if ($recoverForm['eName'].value === '') {
            $nameLabel.setValid(false, '이름을 입력해 주세요.');
            $recoverForm['eName'].focus();
            return;
        }
        if (!nameRegex.test($recoverForm['eName'].value)) {
            $nameLabel.setValid(false, '올바른 이름을 입력해 주세요.');
            $recoverForm['eName'].focus();
            return;
        }
        if ($recoverForm['eBirth'].value === '') {
            $nameLabel.setValid(false, '생년월일을 입력해 주세요.');
            $recoverForm['eBirth'].focus();
            return;
        }
        if ($recoverForm['eGender'].value === '') {
            $nameLabel.setValid(false, '성별을 입력해 주세요.');
            $recoverForm['eGender'].focus();
            return;
        }
        if ($recoverForm['eContactMvno'].value === '-1') {
            $contactLabel.setValid(false, '통신사를 선택해 주세요.');
            $recoverForm['eContactMvno'].focus();
            return;
        }
        if ($recoverForm['eContactSecond'].value === '') {
            $contactLabel.setValid(false, '연락처를 입력해 주세요.');
            $recoverForm['eContactSecond'].focus();
            return;
        }
        if (!contactSecondRegex.test($recoverForm['eContactSecond'].value)) {
            $contactLabel.setValid(false, '올바른 연락처를 입력해 주세요');
            $recoverForm['eContactSecond'].focus();
            return;
        }
        if ($recoverForm['eContactThird'].value === '') {
            $contactLabel.setValid(false, '연락처를 입력해 주세요.');
            $recoverForm['eContactThird'].focus();
            return;
        }
        if (!contactThirdRegex.test($recoverForm['eContactThird'].value)) {
            $contactLabel.setValid(false, '올바른 연락처를 입력해 주세요');
            $recoverForm['eContactThird'].focus();
            return;
        }

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('name', $recoverForm['eName'].value);
        formData.append('birth', $recoverForm['eBirth'].value);
        formData.append('gender', $recoverForm['eGender'].value);
        formData.append('contactMvnoCode', $recoverForm['eContactMvno'].value);
        formData.append('contactFirst', $recoverForm['eContactFirst'].value);
        formData.append('contactSecond', $recoverForm['eContactSecond'].value);
        formData.append('contactThird', $recoverForm['eContactThird'].value);
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {

                return;
            }
            const response = JSON.parse(xhr.responseText);
            switch (response.email) {
                case 'failure':
                    dialog.showSimpleOk('이메일', '가입하지 않으셨거나 이메일에 대한 정보가 없습니다. 다시한번 확인 해주세요.');
                    break;
                default:
                    dialog.showSimpleOk('이메일', `이메일은 ${response.email} 입니다. 로그인 페이지로 이동합니다.`, {
                        onOkCallback: () => location.href = '/user/login'
                    });
            }
        };
        xhr.open('POST', '/user/recover-email');
        xhr.send(formData);
    } else if ($recoverForm['type'].value === 'password') {
        if (!$recoverForm['pEmailCodeSendButton'].hasAttribute('disabled') || !$recoverForm['pEmailCodeVerifyButton'].hasAttribute('disabled')) {
            dialog.showSimpleOk('비밀번호 재설정', '이메일 인증을 완료해 주세요.');
            return;
        }

        const $passwordLabel = $recoverForm.querySelector('label:has(input[name="pPassword"])')

        if ($recoverForm['pPassword'].value === '') {
            $passwordLabel.setValid(false, '비밀번호를 입력해 주세요.');
            $recoverForm['pPassword'].focus();
            return;
        }
        if (!passwordRegex.test($recoverForm['pPassword'].value)) {
            $passwordLabel.setValid(false, '올바른 비밀번호를 입력해 주세요.');
            $recoverForm['pPassword'].focus();
            return;
        }
        if ($recoverForm['pPasswordCheck'].value === '') {
            $passwordLabel.setValid(false, '비밀번호를 한 번 더 입력해 주세요.');
            $recoverForm['pPasswordCheck'].focus();
            return;
        }
        if ($recoverForm['pPassword'].value !== $recoverForm['pPasswordCheck'].value) {
            $passwordLabel.setValid(false, '비밀번호가 일치하지 않습니다.');
            $recoverForm['pPasswordCheck'].focus();
            return;
        }
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('email', $recoverForm['pEmail'].value);
        formData.append('code', $recoverForm['pEmailCode'].value);
        formData.append('salt', $recoverForm['pEmailSalt'].value);
        formData.append('password', $recoverForm['pPassword'].value);
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {
                dialog.showSimpleOk('비밀번호 재설정', '요총을 처리하는 도중 오류가 발생하였습니다. 잠시 후 시도해 주세요.');
                return;
            }
            const response = JSON.parse(xhr.responseText);
            switch (response.result) {
                case 'success':
                    dialog.showSimpleOk('비밀번호', '비밀번호를 재설정하였습니다. 확인 버튼을 클릭하면 로그인 페이지로 이동합니다.', {
                        onOkCallback: () => location.href = '/user/login'
                    });
                    break;
                default:
                    dialog.showSimpleOk('비밀번호 재설정', '알 수 없는 이유로 비밀번호를 재설정하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        };
        xhr.open('POST', '/user/recover-password');
        xhr.send(formData);
    }
};