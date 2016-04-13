// Code generated by dagger-compiler.  Do not edit.
package haseley.abby.info_security_password_creator_and_manager;

import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binding<LogIn>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 *
 * Owning the dependency links between {@code LogIn} and its
 * dependencies.
 *
 * Being a {@code Provider<LogIn>} and handling creation and
 * preparation of object instances.
 *
 * Being a {@code MembersInjector<LogIn>} and handling injection
 * of annotated fields.
 */
public final class LogIn$$InjectAdapter extends Binding<LogIn>
    implements Provider<LogIn>, MembersInjector<LogIn> {
  private Binding<android.app.KeyguardManager> mKeyguardManager;
  private Binding<android.hardware.fingerprint.FingerprintManager> mFingerprintManager;
  private Binding<FingerprintAuthenticationDialogFragment> mFragment;
  private Binding<java.security.KeyStore> mKeyStore;
  private Binding<javax.crypto.KeyGenerator> mKeyGenerator;
  private Binding<javax.crypto.Cipher> mCipher;
  private Binding<android.content.SharedPreferences> mSharedPreferences;

  public LogIn$$InjectAdapter() {
    super("haseley.abby.info_security_password_creator_and_manager.LogIn", "members/haseley.abby.info_security_password_creator_and_manager.LogIn", NOT_SINGLETON, LogIn.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    mKeyguardManager = (Binding<android.app.KeyguardManager>) linker.requestBinding("android.app.KeyguardManager", LogIn.class, getClass().getClassLoader());
    mFingerprintManager = (Binding<android.hardware.fingerprint.FingerprintManager>) linker.requestBinding("android.hardware.fingerprint.FingerprintManager", LogIn.class, getClass().getClassLoader());
    mFragment = (Binding<FingerprintAuthenticationDialogFragment>) linker.requestBinding("haseley.abby.info_security_password_creator_and_manager.FingerprintAuthenticationDialogFragment", LogIn.class, getClass().getClassLoader());
    mKeyStore = (Binding<java.security.KeyStore>) linker.requestBinding("java.security.KeyStore", LogIn.class, getClass().getClassLoader());
    mKeyGenerator = (Binding<javax.crypto.KeyGenerator>) linker.requestBinding("javax.crypto.KeyGenerator", LogIn.class, getClass().getClassLoader());
    mCipher = (Binding<javax.crypto.Cipher>) linker.requestBinding("javax.crypto.Cipher", LogIn.class, getClass().getClassLoader());
    mSharedPreferences = (Binding<android.content.SharedPreferences>) linker.requestBinding("android.content.SharedPreferences", LogIn.class, getClass().getClassLoader());
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(mKeyguardManager);
    injectMembersBindings.add(mFingerprintManager);
    injectMembersBindings.add(mFragment);
    injectMembersBindings.add(mKeyStore);
    injectMembersBindings.add(mKeyGenerator);
    injectMembersBindings.add(mCipher);
    injectMembersBindings.add(mSharedPreferences);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<LogIn>}.
   */
  @Override
  public LogIn get() {
    LogIn result = new LogIn();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<LogIn>}.
   */
  @Override
  public void injectMembers(LogIn object) {
    object.mKeyguardManager = mKeyguardManager.get();
    object.mFingerprintManager = mFingerprintManager.get();
    object.mFragment = mFragment.get();
    object.mKeyStore = mKeyStore.get();
    object.mKeyGenerator = mKeyGenerator.get();
    object.mCipher = mCipher.get();
    object.mSharedPreferences = mSharedPreferences.get();
  }

}
