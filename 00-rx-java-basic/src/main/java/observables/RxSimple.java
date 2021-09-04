package observables;

import com.jakewharton.rxrelay2.BehaviorRelay;
import io.reactivex.*;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.concurrent.TimeUnit;

/**
 * Tipe Observable
 * Relay
 * Subject -> Behavior, Publish, Replay
 * Observable
 * <p>
 * Traits
 * Observable (onNext, onError, onComplete, onSubscribed)
 * Single (onNext, onError)
 * Completable (onComplete, onError)
 * Maybe (onNext/onCompleted, onError)
 */

public class RxSimple {

    private static final CompositeDisposable bag = new CompositeDisposable();

    public static void relay() {
        var behaviorRelay = BehaviorRelay.createDefault("1");

        behaviorRelay.accept("2");

        var disposable = behaviorRelay.subscribe(System.out::println);

        behaviorRelay.accept("3");
        // Behavior = hanya data terbaru saja yang di observe
        // NOTE: Relays will never receive onError & onComplete events
        bag.add(disposable);
    }

    public static void subject() {
        var behaviorSubject = BehaviorSubject.createDefault(1);

        behaviorSubject.onNext(2);
        behaviorSubject.onNext(3);

        var disposable = behaviorSubject.subscribe(
                value -> System.out.println("onNext: " + value),
                error -> System.out.println("error: " + error.getLocalizedMessage()),
                () -> System.out.println("onComplete"),
                d -> System.out.println("Subscribed")
        );

        behaviorSubject.onNext(4);

        // onError
        // behaviorSubject.onError(new IllegalArgumentException("Error"));

        // onComplete
        // behaviorSubject.onComplete();

        behaviorSubject.onNext(5);
        behaviorSubject.onNext(6);

        bag.add(disposable);
    }

    public static void observable() {
        var observable = Observable.create(observer -> {
            System.out.println("Value added");

            // Do background thread
            observer.onNext("Some value");
            observer.onComplete();
        });

        var disposable = observable.subscribe(System.out::println);

        bag.add(disposable);
    }

    public static void createObservable() {
        var observable1 = Observable.just("1");
        var observable2 = Observable.interval(1000, TimeUnit.MILLISECONDS).timeInterval();
        var observable3 = Observable.fromArray(new int[]{1, 2, 3});
    }

    public static void singleTraits() {
        var singleTraits = Single.create(emitter -> emitter.onSuccess(true));

        var disposable = singleTraits.subscribe(value -> {
            if (value instanceof Boolean) {
                if ((boolean) value) {
                    System.out.println("Benar");
                }
            }
        }, error -> {
            System.out.println(error.getLocalizedMessage());
        });

        bag.add(disposable);
    }

    public static void completableTraits() {
        var completableTraits = Completable.create(CompletableEmitter::onComplete);

        var disposable = completableTraits.subscribe(() -> {
            System.out.println("Completed");
        }, error -> System.out.println(error.getLocalizedMessage()));

        bag.add(disposable);
    }

    public static void maybeTraits() {
        var maybeTraits = Maybe.create(emitter -> {
            emitter.onSuccess("Success");
            emitter.onComplete();
        });

        var disposable = maybeTraits.subscribe(
                System.out::println,
                error -> System.out.println(error.getLocalizedMessage()),
                () -> System.out.println("Completed")
        );

        bag.add(disposable);
    }

    public static void clearDisposable() {
        bag.dispose();
    }

}
