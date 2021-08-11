import React from 'react';
import { Navbar, Nav, NavDropdown } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useHistory } from "react-router-dom";
import styled from "styled-components";
import Marginer from '../marginer/Marginer';
import Logo from '../logo/Logo';
import { notification } from 'antd';
import { useTranslation } from 'react-i18next';
import 'flag-icon-css/css/flag-icon.min.css';
import i18next from 'i18next';
import { FaGlobeAmericas } from 'react-icons/fa';
import cookies from 'js-cookie';
import classNames from 'classnames';

const NavbarContainer = styled.div`
  width: 100%;
    
  .collapse {
    justify-content: flex-end;
  }

  .navbar {
    background-color: rgba(0, 0, 0, 0.6);
    position: absolute;
    width: 100%;

    @media (max-width: 991px) { 
      background-color: rgba(0, 0, 0, 0.8);
      position: relative;
    }
  }

  .dropdown-menu {

    @media (max-width: 991px) { 
      min-width: 100%;
    }

    a {
      text-decoration: none;
      color: #000;
    }
  }

  .navbar-toggler {
    color:#f44336;
    margin-right: 3%;
    background-color: #f44336;
  }

  .navbar-nav {
    align-items: center;
    margin-right: 3%;

    .navbar-link {
      a {
        text-decoration: none;
      }
    }
  }
  .nav-link {
    display: flex;
    align-items: center;
    font-size: 17px;
    cursor: pointer;
    text-decoration: none;
    outline: none;
    padding: 5px;
    transition: all 200ms ease-in-out;
  }

  .navbar-light .navbar-nav .nav-link {
    color: #fff;
    padding-left: 3%;
  }

  .navbar-light .navbar-nav .nav-link:focus {
    color: #fff;
  }

  .navbar-light .navbar-nav .nav-link:hover {
    color: #f44336;
  }

  .navbar-light .navbar-nav .active>.nav-link, .navbar-light .navbar-nav .nav-link.active, .navbar-light .navbar-nav .nav-link.show, .navbar-light .navbar-nav .show>.nav-link {
    color: #fff;
  }

  .dropdown-menu {
    &.show {
      a:focus {
        background: #f1f1f1;
      }

      a:hover {
        background: #f1f1f1;
      }
    }
  }

  .nav-item {
    @media (max-width: 991px) { 
        width: 100%;
        margin-left: 3%;
    }
  }

  .language-dropdown {
    align-items: center;
  }

  .language-dropdown-item {
    padding: 0.25rem 0.25rem;
    width: 80px;
    min-width: 0;
  }

  .dropdown-menu {
    margin: 0px;
    padding: 0px;
    min-width: 0;
  }
`;

const NavbarLink = styled.div`
  a {
    display: flex;
    align-items: center;
    font-size: 17px;
    color: #fff;
    cursor: pointer;
    text-decoration: none;
    outline: none;
    padding: 5px;
    transition: all 200ms ease-in-out;
  }

  a:hover {
    color: #f44336;
  }

  @media (max-width: 991px) { 
    width: 100%;
    margin-left: 3%;
    padding-left: 2%;
  }   
`;

const NavigationBar = (props) => {

  const { t } = useTranslation();

  const { user, setUser } = props;
  const history = useHistory();

  const handleLogout = () => {
    localStorage.clear();
    setUser(null);
    notification.success({
      message: `${t('you_are_logged_out')}`
    });
    history.push("/");
  }

  const languages = [
    {
      code: 'be',
      name: 'BY',
      country_code: 'by',
    },
    {
      code: 'ru',
      name: 'RU',
      country_code: 'ru',
    },
    {
      code: 'en',
      name: 'EN',
      country_code: 'gb',
    }
  ]

  const currentLanguageCode = cookies.get('i18next') || 'en'
  const currentLanguage = languages.find((l) => l.code === currentLanguageCode)

  return (
    <NavbarContainer>
      <Navbar expand="lg">
        <Link to="/"><Logo /></Link>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="mr-auto"></Nav>
          <Nav>
            <NavbarLink className="navbar-link"><Link to="/">{t('home')}</Link></NavbarLink>
            <Marginer direction="horizontal" margin={10} />
            <NavbarLink className="navbar-link"><Link to="/brands">{t('car_brands')}</Link></NavbarLink>
            <Marginer direction="horizontal" margin={10} />
            <NavbarLink className="navbar-link"><Link to="/search">{t('search_car')}</Link></NavbarLink>
            <Marginer direction="horizontal" margin={10} />
            <NavbarLink className="navbar-link"><Link to="/about">{t('about')}</Link></NavbarLink>
            <Marginer direction="horizontal" margin={10} />
            <NavbarLink className="navbar-link"><Link to="/contact">{t('contact')}</Link></NavbarLink>             
            <Marginer direction="horizontal" margin={10} />
            <NavDropdown title={t('account')} id="basic-nav-dropdown">
              { user && user.role && user.role.filter(value => value.authority==='ADMIN').length>0 && 
                <>
                  <NavDropdown.Item onClick={() => history.push("/admin")} >{t('dashboard')}</NavDropdown.Item>
                  <NavDropdown.Item onClick={() => history.push("/profile")} >{t('profile')}</NavDropdown.Item>
                  <NavDropdown.Item onClick={handleLogout} >{t('log_out')}</NavDropdown.Item>
                </>
              }
              { user && user.role && user.role.filter(value => value.authority==='USER').length>0 && 
                <>
                  <NavDropdown.Item onClick={() => history.push("/profile")} >{t('profile')}</NavDropdown.Item>
                  <NavDropdown.Item onClick={handleLogout} >{t('log_out')}</NavDropdown.Item>
                </>
              }
              { !(user && user.role && user.role.filter(value => value.authority==='ADMIN').length>0 || 
              user && user.role && user.role.filter(value => value.authority==='USER').length>0) && 
                <>
                  <NavDropdown.Item onClick={() => history.push("/login")} >{t('login')}</NavDropdown.Item>
                  <NavDropdown.Item onClick={() => history.push("/register")} >{t('register_navbar')}</NavDropdown.Item>
                </>
              }            
            </NavDropdown>
            <Marginer direction="horizontal" margin={10} />
            <NavDropdown title={<FaGlobeAmericas />} id="basic-nav-dropdown" className="language-dropdown">
              {languages.map(({code, name, country_code}) => (
                <NavDropdown.Item 
                  className={classNames('language-dropdown-item', {
                    disabled: currentLanguageCode === code,
                  })}
                  key={country_code} onClick={() => i18next.changeLanguage(code)}>
                  <span 
                    style={{
                      opacity: currentLanguageCode === code ? 0.5 : 1,
                    }} 
                    className={`flag-icon flag-icon-${country_code} mx-2`}>
                  </span>
                  {name}
                </NavDropdown.Item>
              ))}
            </NavDropdown>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
    </NavbarContainer>      
  )
}

export default NavigationBar
