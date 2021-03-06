/**
 * generated by Xtext 2.9.0
 */
package org.xtext.example.tfg.myDsl.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.xtext.example.tfg.myDsl.ENTITY;
import org.xtext.example.tfg.myDsl.MyDslPackage;
import org.xtext.example.tfg.myDsl.RELATIONSHIP;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>RELATIONSHIP</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.xtext.example.tfg.myDsl.impl.RELATIONSHIPImpl#getCardinal <em>Cardinal</em>}</li>
 *   <li>{@link org.xtext.example.tfg.myDsl.impl.RELATIONSHIPImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.xtext.example.tfg.myDsl.impl.RELATIONSHIPImpl#getName <em>Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RELATIONSHIPImpl extends MinimalEObjectImpl.Container implements RELATIONSHIP
{
  /**
   * The default value of the '{@link #getCardinal() <em>Cardinal</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCardinal()
   * @generated
   * @ordered
   */
  protected static final String CARDINAL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getCardinal() <em>Cardinal</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCardinal()
   * @generated
   * @ordered
   */
  protected String cardinal = CARDINAL_EDEFAULT;

  /**
   * The cached value of the '{@link #getType() <em>Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected ENTITY type;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RELATIONSHIPImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return MyDslPackage.Literals.RELATIONSHIP;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getCardinal()
  {
    return cardinal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCardinal(String newCardinal)
  {
    String oldCardinal = cardinal;
    cardinal = newCardinal;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MyDslPackage.RELATIONSHIP__CARDINAL, oldCardinal, cardinal));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ENTITY getType()
  {
    if (type != null && type.eIsProxy())
    {
      InternalEObject oldType = (InternalEObject)type;
      type = (ENTITY)eResolveProxy(oldType);
      if (type != oldType)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, MyDslPackage.RELATIONSHIP__TYPE, oldType, type));
      }
    }
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ENTITY basicGetType()
  {
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setType(ENTITY newType)
  {
    ENTITY oldType = type;
    type = newType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MyDslPackage.RELATIONSHIP__TYPE, oldType, type));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MyDslPackage.RELATIONSHIP__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case MyDslPackage.RELATIONSHIP__CARDINAL:
        return getCardinal();
      case MyDslPackage.RELATIONSHIP__TYPE:
        if (resolve) return getType();
        return basicGetType();
      case MyDslPackage.RELATIONSHIP__NAME:
        return getName();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case MyDslPackage.RELATIONSHIP__CARDINAL:
        setCardinal((String)newValue);
        return;
      case MyDslPackage.RELATIONSHIP__TYPE:
        setType((ENTITY)newValue);
        return;
      case MyDslPackage.RELATIONSHIP__NAME:
        setName((String)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case MyDslPackage.RELATIONSHIP__CARDINAL:
        setCardinal(CARDINAL_EDEFAULT);
        return;
      case MyDslPackage.RELATIONSHIP__TYPE:
        setType((ENTITY)null);
        return;
      case MyDslPackage.RELATIONSHIP__NAME:
        setName(NAME_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case MyDslPackage.RELATIONSHIP__CARDINAL:
        return CARDINAL_EDEFAULT == null ? cardinal != null : !CARDINAL_EDEFAULT.equals(cardinal);
      case MyDslPackage.RELATIONSHIP__TYPE:
        return type != null;
      case MyDslPackage.RELATIONSHIP__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (cardinal: ");
    result.append(cardinal);
    result.append(", name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} //RELATIONSHIPImpl
