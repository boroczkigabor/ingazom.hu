function googleSignedIn(googleUser) {
    setAvatarImg(googleUser.getBasicProfile().getImageUrl());

    config.email = googleUser.getBasicProfile().getEmail();
    config.token_id = googleUser.getAuthResponse().id_token;
    config.oauth_provider = 'Google';

    loggedIn(googleSignOut);
}

function googleSignOut() {
    var auth2 = gapi.auth2.getAuthInstance();
        auth2.signOut().then(function () {
            console.log('User signed out.');
            resetAuth();
        });
}

function resetAuth() {
    config.email = undefined;
    config.token_id = undefined;
    config.oauth_provider = undefined;
    document.getElementById('avatar_a').onclick = function() { showModal('authModal'); };
    setAvatarImg('https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y');
}

function loggedIn(logoutFunction) {
    document.getElementById('avatar_a').onclick = logoutFunction;
    hideModal('authModal');

    fetch(config.baseUrl + 'user/login', {
        method: 'POST',
        mode: 'cors',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(createUserObject())
    });
}

function createUserObject() {
    return {
        "email": config.email,
        "access_token": config.token_id,
        "token_issuer": config.oauth_provider
    };
}

function setAvatarImg(imageUrl) {
    var avatarImg = document.getElementById('avatarImg');
    avatarImg.src = imageUrl;
}

function facebookStatusChangeCallback(response) {
    if (response.status === 'connected') {
        FB.api('/me', { fields: 'email' }, function(userInfo) {
            config.email = userInfo.email;
        });
        config.token_id = response.authResponse.accessToken;
        config.oauth_provider = 'Facebook';
        setAvatarImg('https://graph.facebook.com/' + response.authResponse.userID + '/picture');
        loggedIn(facebookLogOut);
    } else if (config.token_id === undefined) {
        resetAuth();
    }
}

function facebookLogOut() {
    FB.logout(function(response) {
        console.log('User signed out.');
        resetAuth();
    });
}