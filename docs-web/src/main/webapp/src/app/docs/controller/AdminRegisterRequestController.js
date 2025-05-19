'use strict';

angular.module('docs')
  .controller('AdminRegisterRequestController', function($scope, Restangular, $log) {
    $scope.requests = [];

    // 加载所有待审核的请求
    $scope.loadRequests = function () {
      $log.info('📥 正在加载待审核请求...');
      Restangular.one('registration-request').one('pending').get()
        .then(function (res) {
          $log.info('✅ 成功获取待审核列表', res);
          $scope.requests = res.requests;
        })
        .catch(function (err) {
          $log.error('❌ 获取失败', err);
        });
    };

    // 审核操作：通过或拒绝
    $scope.handle = function (id, approve) {
      const action = approve ? 'approve' : 'reject';
      $log.info(`⚙️ 正在${approve ? '通过' : '拒绝'}请求：`, id);

      Restangular.one('registration-request', id).post(action)
        .then(function () {
          $log.info(`✅ 已${approve ? '通过' : '拒绝'}：`, id);
          $scope.loadRequests(); // 重新加载
        })
        .catch(function (err) {
          $log.error(`❌ 操作失败：${action}`, err);
        });
    };

    // 初始化加载
    $scope.loadRequests();
  });
