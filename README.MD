[simple-stack](https://github.com/Zhuinden/simple-stack) sample based on [official scoping sample](https://github.com/Zhuinden/simple-stack/tree/781525c0d95d49ad0c13d286ca9c2ddb4f91125b/simple-stack-example-scoping/src/main/java/com/zhuinden/simplestackexamplescoping) 
that uses Dagger + constructor injection to inject scoped services into
fragments.

This approach allows every feature module to be fairly independent from each 
other (for example, only external reference to the [features/words](https://github.com/matejdro/simple-stack-dagger-scoping-sample/tree/master/app/src/main/java/com/matejdro/simple_stack_dagger_demo/features/words) 
folder is [the entry in the MobileActivitySubcomponent](https://github.com/matejdro/simple-stack-dagger-scoping-sample/blob/28987c12823d5ca1168c0106ec932d4820462090/app/src/main/java/com/matejdro/simple_stack_dagger_demo/di/MobileActivitySubcomponent.kt#L12)).

# Annoyances/Issues with this approach

* When [injecting Scoped Services](https://github.com/matejdro/simple-stack-dagger-scoping-sample/blob/28987c12823d5ca1168c0106ec932d4820462090/app/src/main/java/com/matejdro/simple_stack_dagger_demo/features/words/WordListFragment.kt#L21), 
one must remember to use `@ScopedService` annotation. If annotation is left out,
application still compiles and it works at a first glance, but actual
injected service will not be scoped (it will be re-created only for the
injected screen). This could be solved with some added lint checks or
even better, with dagger supportin [qualifiers on constructor injection](https://github.com/google/dagger/issues/1697).
* Adding services requires quite a few [Dagger boilerplate](https://github.com/matejdro/simple-stack-dagger-scoping-sample/blob/28987c12823d5ca1168c0106ec932d4820462090/app/src/main/java/com/matejdro/simple_stack_dagger_demo/features/words/WordsModule.kt). 
This could be solved by creating annotation processor / compiler plugin
to generate this for the user, but I'm wondering if compilation speed
hit is worth it.
* To avoid race conditions with simple-stack's initialization,
`setStateChanger` function is [called on different places](https://github.com/matejdro/simple-stack-dagger-scoping-sample/blob/master/app/src/main/java/com/matejdro/simple_stack_dagger_demo/MainActivity.kt#L41)
depending on whether activity is restored or created from scratch. 