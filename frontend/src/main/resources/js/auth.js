function googleSignedIn(googleUser) {
    var avatarImg = document.getElementById('avatarImg');
    avatarImg.src = googleUser.getBasicProfile().getImageUrl();

    config.token_id = googleUser.getAuthResponse().id_token;

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
    var avatarImg = document.getElementById('avatarImg');

    config.token_id = undefined;
    document.getElementById('avatar_a').onclick = function() { showModal('authModal'); };
    avatarImg.src = 'https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y';
}
