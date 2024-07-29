<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Fintrack - パスワードリセット</title>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>" />
</head>
<body class="zen-old-mincho-regular">
    <div class="login-container">
        <div class="login-box">
            <h1>パスワードリセット</h1>
            <div id="error-message-container" class="error-message" style="display: none;"></div>
            <form id="password-reset-form" class="login-form" action="<c:url value='/password-reset'/>" method="post">
                <div class="form-group">
                    <label for="email">メールアドレス</label>
                    <input type="email" id="email" name="email" required />
                    <span class="error-message" style="display: none;"></span>
                </div>
                <button type="submit" class="login-button">リセットリンクを送信</button>
            </form>
            <p class="login-link"><a href="<c:url value='/login.jsp'/>">ログインページに戻る</a></p>
        </div>
    </div>
<%--     <script src="<c:url value='/js/password-reset.js'/>"></script>
 --%>    <script src="${pageContext.request.contextPath}/js/password-reset.js"></script>
</body>
</html>