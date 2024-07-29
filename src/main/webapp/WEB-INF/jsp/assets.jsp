<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Fintrack - 資産管理</title>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>" />
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
                <h1>資産管理</h1>
            </header>
            <div id="error-message-container" class="error-message" style="display: none;"></div>
            <div id="loading-spinner" class="loading-spinner" style="display: none;">読み込み中...</div>

            <section class="assets-overview">
                <h2>資産概要</h2>
                <div class="assets-summary-container">
                    <div id="assets-summary"></div>
                </div>
            </section>

            <section class="asset-category" id="bank-accounts">
                <h2>銀行口座</h2>
                <form class="asset-form" id="bank-form" action="<c:url value='/addAsset'/>" method="post">
                    <div class="asset-form-inputs">
                        <input type="text" name="name" placeholder="銀行名" required />
                        <span class="error-message" style="display: none;"></span>
                        <input type="number" name="balance" placeholder="残高" required />
                        <span class="error-message" style="display: none;"></span>
                    </div>
                    <input type="hidden" name="category" value="bank" />
                    <button type="submit">追加</button>
                </form>
                <ul class="asset-list" id="bank-list">
                    <c:forEach var="asset" items="${bankAccounts}">
                        <li>${asset.name}: ${asset.balance}円</li>
                    </c:forEach>
                </ul>
            </section>

            <section class="asset-category" id="credit-cards">
                <h2>クレジットカード</h2>
                <form class="asset-form" id="credit-form" action="<c:url value='/addAsset'/>" method="post">
                    <div class="asset-form-inputs">
                        <input type="text" name="name" placeholder="カード名" required />
                        <span class="error-message" style="display: none;"></span>
                        <input type="number" name="balance" placeholder="残高" required />
                        <span class="error-message" style="display: none;"></span>
                    </div>
                    <input type="hidden" name="category" value="credit" />
                    <button type="submit">追加</button>
                </form>
                <ul class="asset-list" id="credit-list">
                    <c:forEach var="asset" items="${creditCards}">
                        <li>${asset.name}: ${asset.balance}円</li>
                    </c:forEach>
                </ul>
            </section>

            <section class="asset-category" id="investments">
                <h2>投資</h2>
                <form class="asset-form" id="investment-form" action="<c:url value='/addAsset'/>" method="post">
                    <div class="asset-form-inputs">
                        <input type="text" name="name" placeholder="投資名" required />
                        <span class="error-message" style="display: none;"></span>
                        <input type="number" name="balance" placeholder="現在価値" required />
                        <span class="error-message" style="display: none;"></span>
                    </div>
                    <input type="hidden" name="category" value="investment" />
                    <button type="submit">追加</button>
                </form>
                <ul class="asset-list" id="investment-list">
                    <c:forEach var="asset" items="${investments}">
                        <li>${asset.name}: ${asset.balance}円</li>
                    </c:forEach>
                </ul>
            </section>

            <section class="asset-category" id="real-estate">
                <h2>不動産</h2>
                <form class="asset-form" id="real-estate-form" action="<c:url value='/addAsset'/>" method="post">
                    <div class="asset-form-inputs">
                        <input type="text" name="name" placeholder="不動産名" required />
                        <span class="error-message" style="display: none;"></span>
                        <input type="number" name="balance" placeholder="推定価値" required />
                        <span class="error-message" style="display: none;"></span>
                    </div>
                    <input type="hidden" name="category" value="real-estate" />
                    <button type="submit">追加</button>
                </form>
                <ul class="asset-list" id="real-estate-list">
                    <c:forEach var="asset" items="${realEstate}">
                        <li>${asset.name}: ${asset.balance}円</li>
                    </c:forEach>
                </ul>
            </section>

            <section class="asset-category" id="other-assets">
                <h2>その他の資産</h2>
                <form class="asset-form" id="other-form" action="<c:url value='/addAsset'/>" method="post">
                    <div class="asset-form-inputs">
                        <input type="text" name="name" placeholder="資産名" required />
                        <span class="error-message" style="display: none;"></span>
                        <input type="number" name="balance" placeholder="推定価値" required />
                        <span class="error-message" style="display: none;"></span>
                    </div>
                    <input type="hidden" name="category" value="other" />
                    <button type="submit">追加</button>
                </form>
                <ul class="asset-list" id="other-list">
                    <c:forEach var="asset" items="${otherAssets}">
                        <li>${asset.name}: ${asset.balance}円</li>
                    </c:forEach>
                </ul>
            </section>

        </main>
    </div>
<%--     <script src="<c:url value='/js/assets.js'/>"></script>
 --%>    <script src="${pageContext.request.contextPath}/js/assets.js"></script>
</body>
</html>