<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Fintrack - アカウント登録</title>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>" />
</head>
<body class="zen-old-mincho-regular">
    <div class="login-container">
        <div class="login-box">
            <div class="logo-container">
                <svg class="logo" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 50">
                    <style>
                        .logo-text { font-family: "Zen Old Mincho", serif; font-weight: bold; }
                    </style>
                    <rect x="0" y="10" width="30" height="30" rx="5" fill="#ffc436"/>
                    <circle cx="15" cy="25" r="10" fill="#ffffff"/>
                    <path d="M15 20 L18 25 L15 30 L12 25 Z" fill="#3a4f7a"/>
                    <text x="40" y="35" class="logo-text" font-size="24" fill="#3a4f7a">Fintrack</text>
                </svg>
            </div>
            <h1>アカウント登録</h1>
            <!-- エラーメッセージコンテナを追加 -->
            <div id="error-message-container" class="error-message" style="display: none;"></div>

            <form id="register-form" class="login-form" action="<c:url value='/register'/>" method="post">
                <div class="form-group">
                    <label for="username">ユーザー名</label>
                    <input type="text" id="username" name="username" required />
                    <span class="error-message" style="display: none;"></span>
                </div>
                <div class="form-group">
                    <label for="email">メールアドレス</label>
                    <input type="email" id="email" name="email" required />
                    <span class="error-message" style="display: none;"></span>
                </div>
                <div class="form-group">
                    <label for="password">パスワード</label>
                    <input type="password" id="password" name="password" required />
                    <span class="error-message" style="display: none;"></span>
                </div>
                <div class="form-group">
                    <label for="confirm-password">パスワード（確認）</label>
                    <input type="password" id="confirm-password" name="confirm-password" required />
                    <span class="error-message" style="display: none;"></span>
                </div>
                <button type="submit" class="login-button">登録</button>
            </form>
            <p class="register-link">すでにアカウントをお持ちの方は<a href="<c:url value='/login.jsp'/>">こちら</a></p>
        </div>
    </div>
<%--     <script src="<c:url value='/js/register.js'/>"></script>
 --%>    <script src="${pageContext.request.contextPath}/js/register.js"></script>
</body>
</html>