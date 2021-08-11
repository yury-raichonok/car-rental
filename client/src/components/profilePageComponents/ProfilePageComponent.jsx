import { useState } from 'react';
import ProfileMenuComponent from './ProfileMenuComponent';
import styled from 'styled-components';
import PageComponent from '../pageComponents/PageComponent';
import { Result, Button, Drawer } from 'antd';
import { Link } from 'react-router-dom';
import MenuKebab from "@kiwicom/orbit-components/lib/icons/MenuKebab";
import { useTranslation } from 'react-i18next';

const ProfilePageContainer = styled.div`
  margin-top: 51px;
  width: 100%;
  min-height: calc( 100vh - 301px );
  display: flex;

  @media (max-width: 991px) { 
    margin-top: 0;
    min-height: calc( 100vh - 306px );
  }
`;

const MenuContainer = styled.div`
  width: 257px;
  border-right: 1px solid #f0f0f0;

  .ant-menu-inline {
    border: none;
  }

  @media (max-width: 991px) { 
    display: none;
  }
`;

const ContentContainer = styled.div`
  width: calc(100vw - 277px);
  height: 100%;

  @media (max-width: 991px) { 
    width: calc(100vw - 70px);
  }
`;

const ShowMenuButton = styled.button`
  width: 50px;
  min-height: calc( 100vh - 306px );
  display: none;

  font-size: 15px;
    color: #000;
    background-color: #fafafa;
    border: none;

    &:hover {
      background-color: #ea5c52;
      color: #fff;
    }

    &:focus {
      box-shadow: none;
    }

  @media (max-width: 991px) { 
    display: block
  }
`;

const ProfilePageComponent = (props) => {

  const { t } = useTranslation();

  const { user, setUser } = props;
  const [drawer, setDrawer] = useState(false); 

  const showDrawer = () => {
    setDrawer(true);
  };

  const onClose = () => {
    setDrawer(false);
  };

  const drawerStyle = {
    padding: 0,
  }

  let component = () => (
    <ProfilePageContainer>
      <ShowMenuButton onClick={showDrawer}>
        <MenuKebab />
      </ShowMenuButton>
      <div>
        <Drawer
          placement="left"
          closable={false}
          onClose={onClose}
          visible={drawer}
          key="left"
          bodyStyle = {drawerStyle}
        >
          <ProfileMenuComponent user={user} />
        </Drawer>
      </div>
      <MenuContainer>
        <ProfileMenuComponent user={user} />
      </MenuContainer>
      <ContentContainer>
        {props.child && props.child()}
      </ContentContainer>
    </ProfilePageContainer>
  );

  return (
    <div>
      { (user && user.role && user.role.filter(value => value.authority==='ADMIN').length>0 || 
      user && user.role && user.role.filter(value => value.authority==='USER').length>0) ? (
        <div>
          <PageComponent setUser={setUser} user={user} child={component}/>
        </div>
      ) : (
        <div>
          <Result
            status="403"
            title="403"
            subTitle={t('sorry_you_are_not_authorized_to_access_this_page')}
            extra={<Button type="primary"><Link to="/">{t('home')}</Link></Button>}
          />
        </div>
        )
      }
    </div>  
  )
}

export default ProfilePageComponent
