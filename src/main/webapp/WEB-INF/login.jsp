<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fintrack - ログイン</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <meta name="google-signin-client_id" content="${googleClientId}">
</head>
<body class="zen-old-mincho-regular">
    <p>Current context path: ${pageContext.request.contextPath}</p>
    <div class="login-container">
        <div class="login-box">
            <div class="logo-container">
                <svg class="logo" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 50">
                    <rect x="0" y="10" width="30" height="30" rx="5" fill="#ffc436"/>
                    <circle cx="15" cy="25" r="10" fill="#ffffff"/>
                    <path d="M15 20 L18 25 L15 30 L12 25 Z" fill="#3a4f7a"/>
                    <text x="40" y="35" class="logo-text" font-size="24" fill="#3a4f7a">Fintrack</text>
                </svg>
            </div>
            <h1>ログイン</h1>
            <c:if test="${not empty errorMessage}">
                <div class="error-message" role="alert">
                    <c:out value="${errorMessage}"/>
                </div>
            </c:if>
            <form id="login-form" class="login-form" action="<c:url value='/login'/>" method="post">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <div class="form-group">
                    <label for="username">メールアドレス</label>
                    <input type="email" id="username" name="username" required autocomplete="email">
                    <span class="error-message" aria-live="polite"></span>
                </div>
                <div class="form-group">
                    <label for="password">パスワード</label>
                    <input type="password" id="password" name="password" required autocomplete="current-password">
                    <span class="error-message" aria-live="polite"></span>
                </div>
                <button type="submit" class="login-button">ログイン</button>
                <div class="g-signin2" data-onsuccess="onSignIn"></div>
            </form>
            <p class="register-link">アカウントをお持ちでない方は<a href="<c:url value='/register.jsp'/>">こちら</a></p>
            <p class="reset-link"><a href="<c:url value='/password-reset.jsp'/>">パスワードを忘れた方はこちら</a></p>
        </div>
    </div>
    <script src="<c:url value='/js/login.js'/>"></script>
</body>
</html>