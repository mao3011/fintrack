<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Fintrack - 高度な分析とレポート</title>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>" />
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body class="zen-old-mincho-regular">
    <div class="app-container">
        <aside class="sidebar">
            <div class="sidebar-header">
                <!-- Sidebarのロゴを表示 -->
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
                <!-- Sidebarのナビゲーションリンクを表示 -->
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
                <h1>高度な分析とレポート</h1>
            </header>

            <!-- エラーメッセージを表示する領域 -->
            <div id="error-message-container" class="error-message" style="display: none;"></div>

            <!-- ローディングスピナーを表示する領域 -->
            <div id="loading-spinner" class="loading-spinner" style="display: none;">読み込み中...</div>

            <section class="analysis-section">
                <h2>資産変動グラフ</h2>
                <div class="graph-controls">
                    <button id="monthly-btn" class="btn">月次</button>
                    <button id="yearly-btn" class="btn">年次</button>
                </div>
                <div class="chart-container">
                    <canvas id="asset-change-chart"></canvas>
                </div>
            </section>

            <section class="analysis-section">
                <h2>支出カテゴリー分析</h2>
                <div class="chart-container">
                    <canvas id="expense-category-chart"></canvas>
                </div>
            </section>

            <section class="balance-sheet-section">
                <h2>貸借対照表</h2>
                <table class="balance-sheet">
                    <!-- 貸借対照表の内容を表示する -->
                </table>
            </section>
        </main>
    </div>
<%--     <script src="<c:url value='/js/financial-analysis.js'/>"></script>--%>
<script src="${pageContext.request.contextPath}/js/financial-analysis.js"></script>
 </body>
</html>