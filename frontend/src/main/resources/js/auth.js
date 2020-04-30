function googleSignedIn(googleUser) {
    setAvatarImg(googleUser.getBasicProfile().getImageUrl());

    config.token_id = googleUser.getAuthResponse().id_token;
    config.oauth_provider = 'Google';

    document.getElementById('avatar_a').onclick = googleSignOut;
    console.log(document.getElementById('avatar_a').onclick);
    hideModal('authModal');
}

function googleSignOut() {
    var auth2 = gapi.auth2.getAuthInstance();
        auth2.signOut().then(function () {
            console.log('User signed out.');
            resetAuth();
        });
}

function resetAuth() {
    config.token_id = undefined;
    config.oauth_provider = undefined;
    document.getElementById('avatar_a').onclick = function() { showModal('authModal'); };
    setAvatarImg('https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y');
}

function setAvatarImg(imageUrl) {
    var avatarImg = document.getElementById('avatarImg');
    avatarImg.src = imageUrl;
}

function facebookStatusChangeCallback(response) {
    if (response.status === 'connected') {
        config.token_id = response.authResponse.accessToken;
        config.oauth_provider = 'Facebook';
        setAvatarImg('https://graph.facebook.com/' + response.authResponse.userID + '/picture');
        document.getElementById('avatar_a').onclick = facebookLogOut;
        hideModal('authModal');
    } else if (config.token_id === undefined) {
        resetAuth();
    }
}

function facebookLogOut() {
    FB.logout(function(response) {
        console.log('User signed out.');
        resetAuth();
    })
}