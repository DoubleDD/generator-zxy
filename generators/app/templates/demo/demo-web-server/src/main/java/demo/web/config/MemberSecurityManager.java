package <%= package %>.web.config;

import com.zxy.common.base.exception.UnprocessableException;
import com.zxy.common.cache.Cache;
import com.zxy.common.cache.redis.Redis;
import com.zxy.common.cache.redis.RedisCacheService;
import com.zxy.common.message.provider.MessageSender;
import com.zxy.common.restful.security.support.oauth.OAuthSecurityManager;
import com.zxy.common.serialize.Serializer;
import com.zxy.product.human.api.MemberService;
import com.zxy.product.human.content.MessageHeaderContent;
import com.zxy.product.human.content.MessageTypeContent;
import com.zxy.product.system.api.permission.MenuService;
import com.zxy.product.system.api.permission.OrganizationService;
import com.zxy.product.system.api.permission.RoleService;
import com.zxy.product.system.content.ErrorCode;
import com.zxy.product.system.entity.Member;
import com.zxy.product.system.entity.Organization;
import com.zxy.product.system.jooq.tables.pojos.MenuEntity;
import com.zxy.product.system.jooq.tables.pojos.RoleEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <%= user %>
 *
 */
@Service
public class MemberSecurityManager extends OAuthSecurityManager<Member> {

    public static final String OAUTH_PROVIDER_APPLICATION_NAME = "oauth-provider";
    public static final String OAUTH_PROVIDER_MODULE_NAME = "oauth";

    private MemberService memberService = null;
    private MenuService menuService;
    private RoleService roleService;
    private OrganizationService organizationService;
    private Cache cache;
    private MessageSender messageSender;

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @Autowired
    public void setOAuthRedisCacheService(Redis redis, Serializer ser){
        RedisCacheService redisCacheService = new RedisCacheService(OAUTH_PROVIDER_APPLICATION_NAME);
        redisCacheService.setRedis(redis);
        redisCacheService.setSerializer(ser);
        this.cache = redisCacheService.create(OAUTH_PROVIDER_MODULE_NAME);
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    protected Member getCurrentUser(String id) {
        Member systemMember = new Member();
        com.zxy.product.human.entity.Member humanMember = memberService.get(id);
        BeanUtils.copyProperties(humanMember, systemMember);

        // 加入头像
        systemMember.setHeadPortrait(humanMember.getMemberDetail().getHeadPortrait());
        systemMember.setHeadPortraitPath(humanMember.getMemberDetail().getHeadPortraitPath());

        // 所属部门
        Organization organization = organizationService.get(humanMember.getOrganizationId()).orElse(null);
        systemMember.setOrganization(organization);

        if (organization==null) {
           throw new UnprocessableException(ErrorCode.CanNotFindOrganization);
        }
        // 根组织
        Organization rootOrganization = organizationService.get(organization.getPath().split(",")[0]).orElse(null);
        systemMember.setRootOrganization(rootOrganization);
        // 所属机构
        organizationService.getBasic(organization.getCompanyId()).ifPresent(companyOrganization -> {
            systemMember.setCompanyOrganization(companyOrganization);
        });

        return systemMember;
    }

    @Override
    protected Cache getCache() {
        return cache;
    }

    @Override
    protected Set<String> getPermissions(String userId) {
        return menuService.findByMemberId(userId).stream().map(MenuEntity::getUri).collect(Collectors.toSet());
    }

    @Override
    protected Set<String> getRoles(String userId) {
        return roleService.findByMemberId(userId).stream().map(RoleEntity::getName).collect(Collectors.toSet());
    }

    @Override
    protected void sendExpiredTimeMessage(String userId, String terminalType) {
        messageSender.send(MessageTypeContent.OAUTH_REFRESH_EXPIRED_TIME,
                MessageHeaderContent.MEMBER_ID, userId,
                MessageHeaderContent.TYPE, terminalType);
    }
}
