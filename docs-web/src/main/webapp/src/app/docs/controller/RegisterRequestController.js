'use strict';

angular.module('docs')
  .controller('RegisterRequestController', function($scope, Restangular, $log, $translate, $dialog) {
    $scope.form = {
      username: '',
      email: '',
      reason: ''
    };
    $scope.success = false;
    $scope.error = false;

    $log.info('📘 RegisterRequestController 初始化');

    $scope.submitRequest = function () {
      $log.info('📤 正在提交注册请求：', $scope.form);

      Restangular.one('registration-request').post('', $scope.form)
        .then(function (response) {
          $log.info('✅ 提交成功', response);
          $scope.success = true;
          $scope.error = false;
          $scope.form = {};

          const btns = [{
            result: 'ok',
            label: $translate.instant('ok'),
            cssClass: 'btn-primary'
          }];
          $dialog.messageBox('✅ Success', '注册请求已成功发送', btns);
        })
        .catch(function (err) {
          $log.error('❌ 提交失败', err);
          $scope.success = false;
          $scope.error = true;

          const btns = [{
            result: 'ok',
            label: $translate.instant('ok'),
            cssClass: 'btn-danger'
          }];
          $dialog.messageBox('❌ 错误', err.data?.message || '提交失败，请检查网络或接口', btns);
        });
    };
  });
