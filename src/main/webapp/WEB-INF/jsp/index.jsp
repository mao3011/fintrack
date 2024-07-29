<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Fintrack - ホーム</title>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>" />
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
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
                <h1>ホーム</h1>
                <div class="date-selector">
                    <button class="date-nav prev-month" aria-label="前月">&lt;</button>
                    <span id="current-date" class="current-date"></span>
                    <button class="date-nav next-month" aria-label="翌月">&gt;</button>
                </div>
            </header>
            <!-- エラーメッセージコンテナを追加 -->
            <div id="error-message-container" class="error-message" style="display: none;"></div>

            <!-- ローディングスピナーを追加 -->
            <div id="loading-spinner" class="loading-spinner" style="display: none;">読み込み中...</div>

            <section class="summary-section">
                <div class="summary-cards">
                    <div class="summary-card income">
                        <h3>収入</h3>
                        <p class="amount" id="income-amount">¥0</p>
                    </div>
                    <div class="summary-card expense">
                        <h3>支出</h3>
                        <p class="amount" id="expense-amount">¥0</p>
                    </div>
                    <div class="summary-card balance">
                        <h3>収支</h3>
                        <p class="amount" id="balance-amount">¥0</p>
                    </div>
                </div>
                <div class="chart-container">
                    <canvas id="expense-chart"></canvas>
                </div>
            </section>

            <!-- 新しい入出金フォームセクション -->
            <div class="layout-container">
                <section class="transaction-form-section">
                    <h2>新しい入出金を追加</h2>
                    <form id="transaction-form">
                        <div class="form-group">
                            <label for="date">日付:</label>
                            <input type="date" id="date" name="date" required />
                            <span class="error-message" style="display: none;"></span>
                        </div>
                        <div class="form-group">
                            <label for="category">カテゴリー:</label>
                            <select id="category" name="category" required>
                                <!-- カテゴリーオプションはJavaScriptで動的に生成 -->
                            </select>
                            <span class="error-message" style="display: none;"></span>
                        </div>
                        <div class="form-group">
                            <label for="amount">金額:</label>
                            <input type="number" id="amount" name="amount" required />
                            <span class="error-message" style="display: none;"></span>
                        </div>
                        <div class="form-group">
                            <label for="payment-method">支払い方法:</label>
                            <select id="payment-method" name="payment-method" required>
                                <!-- 支払い方法オプションはJavaScriptで動的に生成 -->
                            </select>
                            <span class="error-message" style="display: none;"></span>
                        </div>
                        <div class="form-group">
                            <label for="memo">メモ:</label>
                            <textarea id="memo" name="memo"></textarea>
                            <span class="error-message" style="display: none;"></span>
                        </div>
                        <div class="form-group">
                            <label>タイプ:</label>
                            <div class="type-buttons">
                                <button type="button" class="type-button" data-type="収入">収入</button>
                                <button type="button" class="type-button" data-type="支出">支出</button>
                            </div>
                            <input type="hidden" id="type" name="type" required />
                            <span class="error-message" style="display: none;"></span>
                        </div>
                        <button type="submit" class="submit-btn">追加</button>
                    </form>
                </section>

                <section class="calendar-section">
                    <h2>月間カレンダー</h2>
                    <div id="monthly-calendar" class="monthly-calendar">
                        <!-- カレンダーはJavaScriptで動的に生成 -->
                    </div>
                </section>
            </div>
        </main>
    </div>
<%--<script src="<c:url value='/js/index.js'/>"></script>--%>
<script src="${pageContext.request.contextPath}/js/index.js"></script>
</body>
</html>