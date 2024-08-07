<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fintrack - ユーザー設定</title>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
</head>
<body class="zen-old-mincho-regular">
    <div class="app-container">
        <aside class="sidebar">
            <div class="sidebar-header">
                <svg class="logo" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 50">
                    <style>
                        .logo-text { font-family: "Zen Old Mincho", serif; font-weight: bold; }
                    </style>
                    <rect x="0" y="10" width="30" height="30" rx="5" fill="#ffc436"/>
                    <circle cx="15" cy="25" r="10" fill="#ffffff"/>
                    <path d="M15 20 L18 25 L15 30 L12 25 Z" fill="#3a4f7a"/>
                    <text x="40" y="35" class="logo-text" font-size="24" fill="#ffffff">Fintrack</text>
                </svg>
            </div>
            <nav class="sidebar-nav">
                <ul>
                    <li><a href="<c:url value='/index.jsp'/>">ホーム</a></li>
                    <li><a href="<c:url value='/assets.jsp'/>">資産</a></li>
                    <li><a href="<c:url value='/financial-analysis.jsp'/>">高度な分析とレポート</a></li>
                    <li><a href="<c:url value='/settings.jsp'/>">設定</a></li>
                </ul>
            </nav>
        </aside>
        <main class="main-content">
            <header class="main-header">
                <h1>設定</h1>
            </header>
            <section class="settings-section" id="category-management">
                <h2>カテゴリー管理</h2>
                <div class="category-add">
                    <input type="text" id="new-main-category" placeholder="新しい主カテゴリー">
                    <button id="add-main-category" class="btn">追加</button>
                </div>
                <div id="categories-list">
                    <!-- カテゴリーリストはJavaScriptで動的に生成 -->
                </div>
            </section>
            <section class="settings-section" id="payment-method-management">
                <h2>支払い方法管理</h2>
                <div class="payment-method-add">
                    <input type="text" id="new-payment-method" placeholder="新しい支払い方法">
                    <button id="add-payment-method" class="btn">追加</button>
                </div>
                <ul id="payment-methods-list">
                    <!-- 支払い方法リストはJavaScriptで動的に生成 -->
                </ul>
            </section>
            <section class="settings-section" id="budget-setting">
                <h2>月間予算設定</h2>
                <div class="budget-set">
                    <input type="number" id="monthly-budget" placeholder="月間予算">
                    <button id="set-budget" class="btn">設定</button>
                </div>
                <p id="current-budget">現在の月間予算: ¥0</p>
            </section>
        </main>
    </div>
<%--     <script src="<c:url value='/js/settings.js'/>"></script>
 --%>    <script src="${pageContext.request.contextPath}/js/settings.js"></script>
</body>
</html>