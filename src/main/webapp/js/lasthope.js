var showApp = angular.module('showApp', [])

    .controller('mainController', function($scope) {
        $scope.showUpdatePrice = false;
        $scope.showUpdateStatus = false;
        $scope.count = 1;

        $scope.inc = function() {
            $scope.count = $scope.count >= 10 ? 10 : $scope.count + 1;
        }

        $scope.dec = function() {
            $scope.count = $scope.count > 1 ? $scope.count - 1 : 1;
        }
    })

    .controller('paymentController', function($scope) {
        $scope.showPayment = false;

        $scope.paymentClick = function() {
            $scope.showPayment = !$scope.showPayment;
        }
    })

    .controller('menuController', function($scope) {
        $scope.showLeft = false;
        $scope.showRight = false;

        $scope.leftClick = function() {
            $scope.showLeft = !$scope.showLeft;
            $scope.showRight = false;
        }

        $scope.rightClick = function() {
            $scope.showRight = !$scope.showRight;
            $scope.showLeft = false;
        }
    });