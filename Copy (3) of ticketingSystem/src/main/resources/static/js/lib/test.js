//Test fixture
describe('ngQ unit case testing !!!', function() {

    var _myService, $rootScope, myScope, $timeout;
    var ctrl, myScope;
    // load the service's module
    beforeEach(module('app'));

    beforeEach(function() {
        inject(function(_$rootScope_, _$timeout_) {
            $rootScope = _$rootScope_;
            $timeout = _$timeout_;
        });
    });

    beforeEach(inject(['ngQ', function(ngQ) {
        

        var $el = $('#hello');
        myScope = $rootScope.$new();
        window.jasmine.DEFAULT_TIMEOUT_INTERVAL = 11000;
        _myService = ngQ;
        myScope.globalQueue = new _myService();

        myScope.globalQueue.enQueue(function fun3() {
            $timeout(function() {
                
                $el.css("color", "red");
            }, 500);

        });
        myScope.globalQueue.enQueue(function fun2() {
            $timeout(function() {
                
                $el.css("color", "green");
            }, 1000);
        });
        myScope.globalQueue.enQueue(function fun1() {
            $timeout(function() {
                
                $el.css("color", "brown");
            }, 2000);
            return "Test";
        });
        myScope.globalQueue.enQueue(function fun2() {
            $timeout(function() {
                
                $el.css("color", "green");
            }, 1000);
        });
    }]));




    it('queue should have multiple elements', function() {
        expect(myScope.globalQueue.queue.length).toBe(4);
    });

    it('should remove function from queue', function(done) {
        var length = myScope.globalQueue.queue.length;
        myScope.globalQueue.deQueue();
        setTimeout(function() {
            if (length)
                expect(myScope.globalQueue.queue.length).toBe(length - 1);

            else
                expect(myScope.globalQueue.queue.length).toBe(0);
            done();
        }, 1000);

    });

    it('should remove function that matches the name from queue', function(done) {
        var length = myScope.globalQueue.queue.length;

        myScope.globalQueue.deQueue("fun2");

        setTimeout(function() {
            if (length)
                expect(myScope.globalQueue.queue.length).toBe(length - 1);

            else
                expect(myScope.globalQueue.queue.length).toBe(0);
            done();
        }, 1000);


    });

    it('should work', function(done) {

        myScope.globalQueue.initQueue();

        myScope.$digest();
        setTimeout(function() {

            $timeout.flush()
            expect(myScope.globalQueue.queue.length).toBe(0);
            done();
        }, 10000);


    });
});